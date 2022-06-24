package com.example.toilet_seoul

import android.R
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SecondFragment : Fragment(), MainActivity.onBackPressedListener{

    private lateinit var contactViewModel: ContactViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view: View = inflater.inflate(com.example.toilet_seoul.R.layout.contact_setting, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set contactItemClick & contactItemLongClick lambda
        val adapter = ContactAdapter({ contact ->
            val intent = Intent(getActivity(), AddActivity::class.java)
            intent.putExtra(AddActivity.EXTRA_CONTACT_NAME, contact.name)
            intent.putExtra(AddActivity.EXTRA_CONTACT_NUMBER, contact.number)
            intent.putExtra(AddActivity.EXTRA_CONTACT_ID, contact.id)
            startActivity(intent)
        }, { contact ->
            deleteDialog(contact)
        })

        val lm = LinearLayoutManager(getActivity())
        view.findViewById<RecyclerView>(com.example.toilet_seoul.R.id.contact_recyclerview).adapter = adapter
        view.findViewById<RecyclerView>(com.example.toilet_seoul.R.id.contact_recyclerview).layoutManager = lm
        view.findViewById<RecyclerView>(com.example.toilet_seoul.R.id.contact_recyclerview).setHasFixedSize(true)

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        contactViewModel.getAll().observe(viewLifecycleOwner, Observer<List<Contact>> { contacts ->
            adapter.setContacts(contacts!!)
        })

        view.findViewById<Button>(com.example.toilet_seoul.R.id.main_button).setOnClickListener {
            val intent = Intent(getActivity(), AddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun deleteDialog(contact: Contact) {
        val builder = AlertDialog.Builder(getActivity())
        builder.setMessage("Delete selected contact?")
            .setNegativeButton("NO") { _, _ -> }
            .setPositiveButton("YES") { _, _ ->
                contactViewModel.delete(contact)
            }
        builder.show()
    }

    override fun onBackPressed() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        requireActivity().supportFragmentManager.popBackStack()
    }
}