package com.example.kanban_android.domain.services.impl

import com.example.kanban_android.domain.model.User
import com.example.kanban_android.domain.services.base.AuthService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : AuthService {
    override val currentUserId: String get() = firebaseAuth.currentUser?.uid.orEmpty()
    override val hasUser: Boolean get() = firebaseAuth.currentUser != null
    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { User(it.email, it.uid) } ?: User())
                }
            firebaseAuth.addAuthStateListener(listener)
            awaitClose { firebaseAuth.removeAuthStateListener(listener) }
        }

    override suspend fun signUp(email: String, password: String) {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = authResult.user
        user?.uid?.let {
            val userCollection = firebaseFirestore.collection("users")
            val userData = hashMapOf(
                "email" to email
            )
            userCollection.document(it).set(userData)
        }
    }

    override suspend fun authenticate(email: String, password: String) {
        val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        val user = authResult.user
        user?.uid?.let {
            val userCollection = firebaseFirestore.collection("users")
            userCollection.document(it).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (!documentSnapshot.exists()) {
                        val userData = hashMapOf(
                            "email" to email
                        )
                        userCollection.document(it).set(userData)
                    }
                }
        }
    }

    override suspend fun sendRecoveryEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }

    override suspend fun deleteAccount() {
        firebaseAuth.currentUser!!.delete().await()
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }
}