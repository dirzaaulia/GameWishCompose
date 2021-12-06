package com.dirzaaulia.gamewish.repository

import androidx.annotation.WorkerThread
import com.dirzaaulia.gamewish.base.NotFoundException
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import com.dirzaaulia.gamewish.utils.FirebaseConstant.FIREBASE_COLLECTION_NAME
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseRepository     {

    private val auth = Firebase.auth
    //    private val realtimeDatabase = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL)
    private val database = Firebase.firestore

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

    @WorkerThread
    fun addGameToWishlist(uid: String, gameWishlist: GameWishlist) = flow {
        try {
            database.collection(FIREBASE_COLLECTION_NAME)
                .document(uid)
                .collection("game")
                .document(gameWishlist.id.toString())
                .set(gameWishlist)
                .result?.let {
                    emit(ResponseResult.Success(it))
                } ?: run {
                    throw NotFoundException()
                }
        } catch (e: Exception) {
            emit(ResponseResult.Error(e))
        }

    }

    fun removeGameFromWishlist(uid: String, gameWishlist: GameWishlist): Task<Void> {
        return database.collection(FIREBASE_COLLECTION_NAME)
            .document(uid)
            .collection("game")
            .document(gameWishlist.id.toString())
            .delete()
    }


    suspend fun getAllGameWishlist(uid: String): QuerySnapshot? {
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