package binar.academy.chapter5_topik2

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import binar.academy.chapter5_topik2.databinding.ActivityMainBinding
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCheck.setOnClickListener {
           normalPermission()
        }

        binding.btnCheckStorage.setOnClickListener {
            dangerousPermisson()
        }
    }

    private fun checkInternet(context: Context) : Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when{
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    private fun normalPermission(){
        if (checkInternet(this)){
            Glide.with(this)
                .load("https://img.okezone.com/content/2021/01/22/298/2349008/apel-cocok-untuk-menu-diet-ini-nutrisi-yang-terkandung-ks6tWLGNyy.jpg")
                .into(binding.gambar)
        }else{
            binding.gambar.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }

    private fun dangerousPermisson() {
        val permission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permission == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission storage diizinkan", Toast.LENGTH_LONG).show()
            intentGallery()
        }else{
            Toast.makeText(this, "Permission storage tidak diizinkan", Toast.LENGTH_LONG).show()
            requestStoragePermission()
        }
    }

    private fun intentGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivity(intent)
    }

    private fun requestStoragePermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 201)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 201){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE){
                Toast.makeText(this, "Permission storage diizinkan", Toast.LENGTH_LONG).show()
                intentGallery()
            }else{
                Toast.makeText(this, "Permission storage tidak diizinkan", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this, "Request code invalid!", Toast.LENGTH_LONG).show()
        }
    }
}