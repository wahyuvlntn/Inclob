package com.example.inclob

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SelesaiFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelesaiFragment : Fragment() {
    private lateinit var viewModel: PendaftaranViewModel
    private lateinit var tvSelesai: TextView
    private lateinit var auth: FirebaseAuth
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = "             Selesai"
            // Tambahkan tombol kembali (optional)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        auth = Firebase.auth
        val db = Firebase.firestore
        Log.d("SelesaiFragment", "onViewCreated: Data from ViewModel: ${viewModel.userData}")
        val btnLanjut = view.findViewById<Button>(R.id.btn_lanjut)
        btnLanjut.setOnClickListener {
            viewModel.dataSavedEvent.observe(viewLifecycleOwner) { dataSaved ->
                if (dataSaved) {
                    val userData = viewModel.userData

                    val user = hashMapOf(
                        "nama" to userData?.nama,
                        "username" to userData?.username,
                        "noTelp" to userData?.noTelp,
                        "email" to userData?.email,
                        "tglLahir" to userData?.tglLahir,
                        "jenisKelamin" to userData?.jenisKelamin
                    )

                    db.collection("users")
                        .add(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d("pengecekanbenar", "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w("pengecekansalah", "Error adding document", e)
                        }

                    userData?.email?.let { it1 -> userData?.password?.let { it2 ->
                        userData?.nama?.let { it3 ->
                            register(it1,
                                it2, it3
                            )

                        }
                    } }

                }
            }

        }


    }

    private fun register(email:String,password:String,nama:String) {
        val intent = Intent(activity, HomeActivity::class.java)
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val userUpdateProfile = userProfileChangeRequest {
                        displayName = nama
                    }
                    val user = task.result.user
                    user!!.updateProfile(userUpdateProfile)
                        .addOnCompleteListener {
                            startActivity(intent)

                        }

                }

            }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(PendaftaranViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selesai, container, false)
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SelesaiFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SelesaiFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}