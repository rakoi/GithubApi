package com.example.rakoi.foodapp

import android.app.ProgressDialog
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    var url:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        searchbutton.setOnClickListener{
            var username:String=usernamefield.text.toString()
             url="https://api.github.com/users/"+username

            if(usernamefield.text.isEmpty()){
                Toast.makeText(applicationContext,"Enter Username",Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(applicationContext,"Please wait",Toast.LENGTH_LONG).show()
                MyAsyncTask().execute(url)
            }

        }

    }

    inner class MyAsyncTask:AsyncTask<String,String,String>(){

        override fun onPreExecute() {
            //before task is done

        }


        override fun doInBackground(vararg params: String?): String {

            try {

                var url=URL(url)

                var urlConnect=url.openConnection() as HttpURLConnection
                urlConnect.connectTimeout=7000

                var inputString:String=StreamtoString(urlConnect.inputStream)
                publishProgress(inputString)


            }catch (ex:Exception){
              //  Toast.makeText(applicationContext,"Try again later",Toast.LENGTH_LONG).show()
                ex.printStackTrace()
            }
            return ""
        }

        override fun onProgressUpdate(vararg values: String?) {
           try {
                var json=JSONObject(values[0])

               names.text=json.getString("name").toString()
                company.text=json.getString("company").toString()
                followers.text=json.getString("followers").toString()
                following.text=json.getString("following").toString()
                repositories.text=json.getString("public_repos").toString()
                var image:String=json.getString("avatar_url").toString()
                Glide.with(this@MainActivity).load(image).into(profilepicture)
               // Picasso.with(applicationContext).load(image).into(profilepicture)
               if(json.getString("hireable").toString()=="null"){
                hireable.text="Unknown"

                }else{
                    hireable.text="Hireable"
                }
               //progressDialog!!.dismiss()
           }catch (ex:Exception){
            ex.printStackTrace()
           }
            //progressDialog!!.dismiss()
        }

        override fun onPostExecute(result: String?) {
            //After Task done
        }

    }
    fun StreamtoString(inputStream:InputStream):String{
        var bufferReader=BufferedReader(InputStreamReader(inputStream))
        var line:String
        var allString:String=""

        try {
                do{
                    line=bufferReader.readLine()
                    if (line!=null){
                        allString+=line
                    }
                }while (line!=null)
                inputStream.close()
        }catch (ex:Exception){
            ex.printStackTrace()
        }

        return allString
    }
}
