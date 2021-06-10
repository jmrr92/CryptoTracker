package com.example.cryptotracker

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"

    //Declaramos variables
    private var email by Delegates.notNull<String>()
    private var pass by Delegates.notNull<String>()
    private lateinit var enterEmail: EditText
    private lateinit var enterPass: EditText
    private lateinit var mProgressBar: ProgressDialog

    //Creamos nuestra variable para hacer login en firebase
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        iniciar()
    }

    //Creamos un método para iniciar el diseño y el login de firebase
    private fun iniciar() {
        enterEmail = findViewById(R.id.enterEmail)
        enterPass = findViewById(R.id.enterPass)
        mProgressBar = ProgressDialog(this)
        mAuth = FirebaseAuth.getInstance()
    }

    //Iniciamos sesión usando firebase
    private fun loginUser() {
        //Obtenemos usuario y contraseña
        email = enterEmail.text.toString()
        pass = enterPass.text.toString()
        //Comprobamos que los campos no estén vacíos
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {

            //Mostramos la información en un progressDialog
            mProgressBar.setMessage("Accediendo...")
            mProgressBar.show()

            //Iniciamos sesión con el método signIn y enviamos usuario y contraseña
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
                    //Verificamos que la tarea se ejecutó correctamente
                        task ->
                    if (task.isSuccessful) {
                        //Si los campos introducidos son correctos, nos llevará a la pantalla principal de la App
                        goHome() //Creamos nuestro método goHome para ir a la pantalla principal
                    }
                    else {
                        //En caso contrario le avisamos al usuario del error
                        Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        else {
            Toast.makeText(this, "Introduce tus credenciales", Toast.LENGTH_SHORT).show()
        }
    }


    private fun goHome() {
        //Ocultamos el progress
        mProgressBar.hide()
        //Nos vamos a Home
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

//Creamos métodos con el mismo nombre de nuestros botones en el atributo onclick para llevarnos a las diferentes clases

    //Llamamos al método para hacer login
    fun login(view: View) {
        loginUser()
    }

    //Para ir a la activity de olvidar contraseña
    fun forgotPassword(view: View) {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }

    // Para ir a la activity de registro
    fun register(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}
