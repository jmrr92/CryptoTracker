package com.example.cryptotracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth


class ForgotPasswordActivity : AppCompatActivity() {

    //Declaramos variables
    private var email: EditText? = null
    private var btnEnviar: Button? = null

    //Autenticación firebase
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        iniciar()
    }

    //Iniciamos las vistas
    private fun iniciar() {
        email = findViewById<View>(R.id.enterEmail) as EditText
        btnEnviar = findViewById<View>(R.id.btnEnviar) as Button
        mAuth = FirebaseAuth.getInstance()
        btnEnviar!!.setOnClickListener { sendPasswordResetEmail() }
    }

    //Enviaremos la contraseña al correo en caso de que se haya olvidado conectando la app con Firebase
    private fun sendPasswordResetEmail() {
        val email = email?.text.toString()
        if (!TextUtils.isEmpty(email)) {
            mAuth!!
                .sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    //Enviamos las tareas. Para que sea exitoso el email debe de estar en la base de datos de Firebase.
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Email Enviado", Toast.LENGTH_SHORT).show()
                        goMain()
                    }
                    else {
                        Toast.makeText(this, "No se encontró el usuario con este correo", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        else {
            Toast.makeText(this, "Agregue el correo", Toast.LENGTH_SHORT).show()
        }
    }

    //Nos vamos al menu de login
    private fun goMain() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
