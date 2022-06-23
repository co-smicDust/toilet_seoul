package com.example.toilet_seoul

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.toilet_seoul.databinding.ActivityQueryBinding
import com.google.firebase.database.*

class QueryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQueryBinding
    private lateinit var database: DatabaseReference

    private val toiletAdapter: ToiletAdapter by lazy {
        ToiletAdapter(resultList)
    }

    val toiletList = ArrayList<Toilet>()
    val resultList = ArrayList<Toilet>()

    private lateinit var toilet:Toilet

    var checked: String? = null
    private lateinit var arr: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQueryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initializing recyclerview
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerToilet.layoutManager = llm
        binding.recyclerToilet.adapter = toiletAdapter

        database = FirebaseDatabase.getInstance().getReference("toilet")

        checked = intent.getStringExtra("checked")
        arr = checked?.split("/")!!
    }

    private fun unisexFilter() {
        if (toilet.unisexToiletYn == "N") { toiletList.add(toilet) }
    }

    private fun menDisabledFilter() {
        if (toilet.menHandicapToiletBowlNumber!! > 0 || toilet.menHandicapUrinalNumber!! > 0) { toiletList.add(toilet) }
    }

    private fun womenDisabledFilter() {
        if (toilet.ladiesHandicapToiletBowlNumber!! > 0) { toiletList.add(toilet) }
    }

    private fun menWithChildrenFilter() {
        if (toilet.menChildrenToiletBowlNumber!! > 0 || toilet.menChildrenUrinalNumber!! > 0) { toiletList.add(toilet) }
    }

    private fun womenWithChildrenFilter() {
        if (toilet.ladiesChildrenToiletBowlNumber!! > 0) { toiletList.add(toilet) }
    }

    override fun onStart() {
        super.onStart()

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (i in dataSnapshot.children) {
                        toilet = i.getValue(Toilet::class.java)!!

                        if ("notUnisex" in arr)
                            unisexFilter()
                        if ("menDisabled" in arr)
                            menDisabledFilter()
                        if ("womenDisabled" in arr)
                            womenDisabledFilter()
                        if ("menWithChildren" in arr)
                            menWithChildrenFilter()
                        if ("womenWithChildren" in arr)
                            womenWithChildrenFilter()

                    }
                    val result = toiletList.groupingBy { it }.eachCount().filter { it.value == (arr.size -1) }.keys
                    resultList.addAll(result)

                    if (resultList.isEmpty()){
                        Toast.makeText(applicationContext, "검색 결과가 없습니다.", Toast.LENGTH_LONG).show()
                    } else {
                        toiletAdapter.submitList(resultList)
                        binding.recyclerToilet.adapter = toiletAdapter
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onStop() {
        super.onStop()
    }
}