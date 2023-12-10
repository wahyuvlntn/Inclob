package com.example.inclob.pekerjaanfrag

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.inclob.R
import com.example.inclob.data.PdfFile
import com.example.inclob.data.Pekerjaan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MelamarTahap1Fragment : Fragment() {
    private var documentId: String? = null
    private var pdfFileUri: Uri? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storageReference= FirebaseStorage.getInstance().reference.child("pdfs ")
        documentId = arguments?.getString("docId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_melamar_tahap1, container, false)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            pdfFileUri = uri
            displayFileName(uri)
        }

    private fun displayFileName(uri: Uri?) {
        val fileNameTextView = view?.findViewById<TextView>(R.id.fileNameTextView)
        val cvPdf = view?.findViewById<CardView>(R.id.cv_pdf)
        fileNameTextView?.text = uri?.let { DocumentFile.fromSingleUri(requireContext(), it)?.name }
        cvPdf?.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        documentId?.let { fetchPekerjaanDetails(it) }

        val btnUpload = view.findViewById<LinearLayout>(R.id.btn_upload)
        btnUpload.setOnClickListener {
            launcher.launch("application/pdf")
            btnUpload.visibility = View.GONE
        }

        val btnLamar = view.findViewById<Button>(R.id.btn_lamar)
        btnLamar.setOnClickListener {

            if(pdfFileUri!=null){
                uploadToFirestore(pdfFileUri)
            }else {
                Toast.makeText(requireContext(), "Masukkan CV terlebih dahulu",Toast.LENGTH_SHORT).show()
            }
            val fragThree = PeninjauanBerkasFragment()
            val fragManager = fragmentManager

            fragManager?.beginTransaction()?.apply {
                replace(R.id.content,fragThree, PeninjauanBerkasFragment::class.java.simpleName)
                commit()
            }

        }
    }

    private fun uploadToFirestore(uri: Uri?) {
        val user = auth.currentUser
        user?.let {
            val pdfFileName = uri?.let { DocumentFile.fromSingleUri(requireContext(), it)?.name }
            val mStorageRef = storageReference.child("${System.currentTimeMillis()}/$pdfFileName")
            uri?.let {
                mStorageRef.putFile(it).addOnSuccessListener { _ ->
                    mStorageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val userCollection = firestore.collection("users")
                        val userDocument = userCollection.document(user.uid)
                        val melamarData = mapOf(
                            "status1" to "pending",
                            "status2" to "none",
                            "idPekerjaan" to documentId
                        )
                        userDocument.collection("melamar").add(melamarData)
                            .addOnSuccessListener { melamarDocumentReference ->

                                // Buat document pada collection "pdfs" di dalam document "melamar"
                                val pdfFile = pdfFileName?.let { it1 -> PdfFile(it1, downloadUri.toString()) }
                                pdfFile?.let {
                                    melamarDocumentReference.collection("pdfs").add(it)
                                        .addOnSuccessListener {
                                            // Saat upload ke Firestore berhasil, reset pdfFileUri menjadi null
                                            pdfFileUri = null
                                            Toast.makeText(
                                                requireContext(),
                                                "CV berhasil diunggah ke Firestore",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }.addOnFailureListener { e ->
                                            Toast.makeText(
                                                requireContext(),
                                                "Gagal mengunggah CV ke Firestore: $e",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                            }.addOnFailureListener { e ->
                                Toast.makeText(
                                    requireContext(),
                                    "Gagal membuat document pada collection 'melamar': $e",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        // Masukkan pdfFile ke Firestore, di bawah ini adalah contoh penggunaan Firestore



                    }
                }.addOnProgressListener {
                    // Tambahkan fungsi pembaruan progres di sini jika diperlukan
                }.addOnFailureListener { e ->
                    // Tambahkan penanganan kegagalan di sini jika diperlukan
                    Toast.makeText(
                        requireContext(),
                        "Gagal mengunggah CV ke Storage: $e",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }



    companion object {
        private const val TAG = "MelamarTahap1Fragment"
        // Function to create a new instance of the fragment with the document ID as an argument
        fun newInstance(documentId: String?): MelamarTahap1Fragment {
            val fragment = MelamarTahap1Fragment()
            val args = Bundle()
            args.putString("docId", documentId)
            fragment.arguments = args
            return fragment
        }
    }
    private fun fetchPekerjaanDetails(documentId: String) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("pekerjaan")
            .whereEqualTo("id",documentId)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Handle case when document doesn't exist
                    // Misalnya, tampilkan pesan bahwa dokumen tidak ditemukan
                    Log.d("FirestoreData", "Dokumen tidak ditemukan")
                    return@addOnSuccessListener
                }

                // Hanya mengambil satu dokumen karena seharusnya ID dokumen unik
                val document = documents.documents[0]

                // Map data dokumen ke model (gantilah Pekerjaan::class.java dengan kelas model yang sesuai)
                val pekerjaan = document.toObject(Pekerjaan::class.java)

                // Update TextView elements with data from the Pekerjaan model
                view?.findViewById<TextView>(R.id.tv_title)?.text = pekerjaan?.title
                val image = pekerjaan?.gambar

                image.let {
                    // Menggunakan Glide untuk memuat gambar dari URL ke ImageView
                    view?.findViewById<ImageView>(R.id.iv_pekerjaan)?.let { it1 ->
                        Glide.with(this)
                            .load(it)
                            .into(it1)
                    }
                }
                view?.findViewById<TextView>(R.id.tv_perusahaan)?.text = pekerjaan?.perusahaan
                view?.findViewById<TextView>(R.id.tv_kota)?.text = pekerjaan?.kota

            }
            .addOnFailureListener { exception ->
                // Handle error
                // You might want to show an error message or log the exception
            }
    }
}
