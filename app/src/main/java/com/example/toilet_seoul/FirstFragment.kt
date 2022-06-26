package com.example.toilet_seoul

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FirstFragment : Fragment(), MainActivity.onBackPressedListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        //setHasOptionsMenu(true)
        //(activity as AppCompatActivity).supportActionBar?.title = "My Title"
        return inflater.inflate(R.layout.personal_information, container, false)
    }

    override fun onBackPressed() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        //requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database: DatabaseReference = Firebase.database.reference
        val currentUser = Firebase.auth.currentUser
        val userRef = database.child("User").child(currentUser?.uid.toString())

        view.findViewById<Button>(R.id.join_button).setOnClickListener {
            val input = view.findViewById<EditText>(R.id.join_name).text
            if (input.toString() != "") {
                userRef.child("userNm").setValue(input.toString()).addOnSuccessListener {
                    Toast.makeText(this.context, "닉네임이 변경되었습니다.", Toast.LENGTH_SHORT).show()
                    input.clear()
                }
            } else {
                Toast.makeText(this.context, "새 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()

            }
        }

    }
}