package com.dirzaaulia.gamewish.repository

import androidx.annotation.WorkerThread
import com.dirzaaulia.gamewish.base.NotFoundException
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.utils.FirebaseConstant.FIREBASE_COLLECTION_NAME
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseRepository     {

    private val auth = Firebase.auth
    //    private val realtimeDatabase = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL)
    private val database = Firebase.firestore
    private val settings = firestoreSettings {
        isPersistenceEnabled = false
    }

    fun getFirebaseAuth(): FirebaseAuth {
        return auth
    }

    fun getFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    fun getGoogleLoginStatus(): Boolean {
        return auth.currentUser != null
    }

    fun authGoogleLogin(idToken: String): AuthCredential {
        return GoogleAuthProvider.getCredential(idToken, null)
    }

    suspend fun signIn(credential: AuthCredential) {
        auth.signInWithCredential(credential).await()
    }

    @WorkerThread
    fun getUserAuthId() = flow {
        try {
            auth.currentUser?.let {
                emit(ResponseResult.Success(it.uid))
            } ?: run {
                throw NotFoundException()
            }
        } catch (e: Exception) {
            emit(ResponseResult.Error(e))
        }
    }

    fun addToWishlist(uid: String, wishlist: Wishlist) {
        database.collection(FIREBASE_COLLECTION_NAME)
            .document(uid)
            .collection("game")
            .add(wishlist)
            .addOnSuccessListener {
                Timber.i("Game added to Firestore")
            }
            .addOnFailureListener {
                Timber.i("Something went wrong went adding Game to Firestore : ${it.message}")
            }
    }

    suspend fun getAllWishlist(uid: String): QuerySnapshot? {
        return database.collection(FIREBASE_COLLECTION_NAME)
            .document(uid)
            .collection("game")
            .get()
            .addOnSuccessListener {
                Timber.i("${it.size()}")
            }
            .addOnFailureListener {
                Timber.i(it )
            }
            .await()
    }
}