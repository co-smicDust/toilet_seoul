package com.example.toilet_seoul

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private val mainFragment = MapFragment()

    //뒤로가기 Listener역할을 할 Interface 생성
    interface onBackPressedListener {
        fun onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar) // toolBar를 통해 App Bar 생성
        setSupportActionBar(toolbar) // 툴바 적용

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.navi_menu) // 홈버튼 이미지 변경
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바에 타이틀 안보이게

        setNavigationDrawer() // call method

        if (savedInstanceState == null) {

            supportFragmentManager.beginTransaction()
                .replace(R.id.frame, mainFragment)
                .commit()
        }

        val bundle = intent.getBundleExtra("bundle")

        if (bundle != null && bundle.containsKey("toilet")) {
            val toilet = bundle.getSerializable("toilet") as Toilet
            val clicked = bundle.getBoolean("clicked", false)
            val send = Bundle()
            send.putSerializable("toilet", toilet)
            send.putBoolean("searching", clicked)

            if (clicked){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, mainFragment)
                    .commit()

                mainFragment.arguments = send
            }
        }

        val args: Bundle? = intent.getParcelableExtra("args")

        if (args != null && args.containsKey("latlng")) {
            val position: LatLng = args.getParcelable("latlng")!!
            val showRoute = args.getBoolean("showRoute", false)

            val pass = Bundle()
            pass.putParcelable("latlng", position)
            pass.putBoolean("showRoute", showRoute)

            if (showRoute){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, mainFragment)
                    .commit()

                mainFragment.arguments = pass
            }
        }

        //비상연락 버튼클릭이벤트 - DangerCall (원래는 상단바에 플로팅 버튼, 후기창으로 옮김)
        //myContactButton.setOnClickListener { onMyContactButtonClick() }

    }

    private fun setNavigationDrawer() {
        val dLayout: DrawerLayout = findViewById(R.id.drawer_layout) // initiate a DrawerLayout
        val navView: NavigationView = findViewById(R.id.nav_view) // initiate a Navigation View

        // implement setNavigationItemSelectedListener event on NavigationView
        navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            var frag: Fragment? = null // create a Fragment Object
            val itemId = menuItem.itemId // get selected menu item's id
            // check selected menu item's id and replace a Fragment Accordingly
            when (itemId) {
                R.id.first -> {
                    frag = FirstFragment()
                }
                R.id.second -> {
                    frag = SecondFragment()
                }
                R.id.third -> {
                    frag = ThirdFragment()
                }
                R.id.go_to_main -> {
                    frag = MapFragment()
                }
            }
            // display a toast message with menu item's title
            Toast.makeText(applicationContext, menuItem.title, Toast.LENGTH_SHORT).show()
            if (frag != null) {
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame, frag) // replace a Fragment with Frame Layout
                transaction.commit() // commit the changes
                dLayout.closeDrawers() // close the all open Drawer Views
                return@OnNavigationItemSelectedListener true
            }
            false
        })
    }

    // 툴바 메뉴 버튼이 클릭 됐을 때 실행하는 함수
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //왼쪽 바 상단 닉네임 불러오기
        val database: DatabaseReference = Firebase.database.reference
        val currentUser = Firebase.auth.currentUser
        val userRef = database.child("User").child(currentUser?.uid.toString()).child("userNm")

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                findViewById<TextView>(R.id.text).text = dataSnapshot.value.toString() + "님"
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        val dLayout: DrawerLayout = findViewById(R.id.drawer_layout) // initiate a DrawerLayout

        // 클릭한 툴바 메뉴 아이템 id 마다 다르게 실행하도록 설정
        when(item.itemId){
            android.R.id.home->{
                // 햄버거 버튼 클릭시 네비게이션 드로어 열기
                dLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //fragment에서 상속 - 뒤로가기
    //프레그먼트에서 뒤로가기 한번 누르면 메인액티비티(지도화면)으로
    private var backPressedTime : Long = 0
    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments
        for (fragment in fragmentList) {
            if (fragment is onBackPressedListener) {
                (fragment as onBackPressedListener).onBackPressed()
                return
            }
        }
        //두 번 클릭시 어플 종료
        // 2초내 다시 클릭하면 앱 종료
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            finish()
            return
        }
        // 처음 클릭 메시지
        Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }
}