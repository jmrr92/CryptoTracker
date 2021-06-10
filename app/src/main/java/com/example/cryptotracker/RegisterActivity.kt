package com.example.cryptotracker

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.firebase.auth.*
import com.google.firebase.database.*
import kotlin.properties.Delegates

class RegisterActivity : AppCompatActivity() {
    private lateinit var barraProgreso: ProgressDialog
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var mAuth: FirebaseAuth
    private lateinit var introNombre: EditText
    private lateinit var introApellido: EditText
    private lateinit var introEmail: EditText
    private lateinit var introPass: EditText

    //Variables
    private var nombre by Delegates.notNull<String>()
    private var apellido by Delegates.notNull<String>()
    private var email by Delegates.notNull<String>()
    private var pass by Delegates.notNull<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        iniciar()
    }

    //Creamos un método para iniciar el diseño y el login de firebase
    private fun iniciar() {
        //Llamamos nuestras vistas
        introNombre = findViewById(R.id.introNombre)
        introApellido = findViewById(R.id.introApellido)
        introEmail = findViewById(R.id.introEmail)
        introPass = findViewById(R.id.introPass)
        //Creamos nuestro progressDialog
        barraProgreso = ProgressDialog(this)

        //Creamos una instancia para guardar los datos nuestra base  de datos
        database = FirebaseDatabase.getInstance()
        //Creamos una instancia para crear nuestra autenticación y guardar el usuario
        mAuth = FirebaseAuth.getInstance()

        //Leemos o escribimos en una ubicación específica la base de datos
        databaseReference = database.reference.child("Users")
    }

    //Método para crear una nueva cuenta
    private fun createNewAccount() {

        //Esto nos dará los datos del texto que escribamos
        nombre = introNombre.text.toString()
        apellido = introApellido.text.toString()
        email = introEmail.text.toString()
        pass = introPass.text.toString()

        //Comprobamos que los campos estén llenos
        if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(apellido) &&
            !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {

            //Añadimos información con una progressBar
            barraProgreso.setMessage("Usuario registrado...")
            barraProgreso.show()

            //Damos de alta al usuario con el correo y contraseña en firebase
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
                //Una vez dada de alta la cuenta vamos, damos de alta la información en la BBDD
                // Obtenemos el id del usuario con que accedió con currentUser
                val user: FirebaseUser = mAuth.currentUser!!
                //Enviaremos email de verificación a la cuenta del usuario
                verifyEmail(user);

                //Damos de alta al usuario
                val currentUserDb = databaseReference.child(user.uid)

                //Volvemos a Home
                updateUserInfoAndGoHome()
            }.addOnFailureListener {
                //Si el registro es erróneo nos lo mostrará en pantalla
                Toast.makeText(this, "Error en la autenticación.", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(this, "Algunos campos están incompletos", Toast.LENGTH_SHORT).show()
        }
    }

    //Llamamos el método de crear cuenta en el registro
    fun register(view: View) {
        createNewAccount()
    }

    private fun verifyEmail(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener(this) {
            //Comprobamos que la tarea se realizó correctamente o en caso contrario advertimos del error
                task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Email " + user.getEmail(), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al verificar el correo ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUserInfoAndGoHome() {
        //Nos vamos a la actividad home
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        //Ocultamos la progressBar
        barraProgreso.hide()
    }
}