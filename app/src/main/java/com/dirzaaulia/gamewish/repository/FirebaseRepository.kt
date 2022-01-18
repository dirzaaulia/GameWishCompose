package com.dirzaaulia.gamewish.repository

import androidx.annotation.WorkerThread
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import com.dirzaaulia.gamewish.data.model.wishlist.MovieWishlist
import com.dirzaaulia.gamewish.utils.FirebaseConstant.FIREBASE_COLLECTION_NAME
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseRepository     {

    private val auth = Firebase.auth
    private val database = Firebase.firestore

    fun getFirebaseAuth(): FirebaseAuth {
        return auth
    }

    suspend fun signIn(credential: AuthCredential) {
        auth.signInWithCredential(credential).await()
    }

    @WorkerThread
    fun addGameToWishlist(uid: String, gameWishlist: GameWishlist) = flow {
        try {
            val task = database.collection(FIREBASE_COLLECTION_NAME)
                .document(uid)
                .collection("game")
                .document(gameWishlist.id.toString())
                .set(gameWishlist)

            emit(ResponseResult.Success(task))
        } catch (e: Exception) {
            emit(ResponseResult.Error(e))
        }
    }

    @WorkerThread
    fun addMovieToWishlist(uid: String, movieWishlist: MovieWishlist) = flow {
        try {
            val task = database.collection(FIREBASE_COLLECTION_NAME)
                .document(uid)
                .collection("movie")
                .document(movieWishlist.id.toString())
                .set(movieWishlist)

            emit(ResponseResult.Success(task))
        } catch (e: Exception) {
            emit(ResponseResult.Error(e))
        }
    }

    @WorkerThread
    fun removeGameFromWishlist(uid: String, gameWishlist: GameWishlist) = flow {
        try {
            val task = database.collection(FIREBASE_COLLECTION_NAME)
                .document(uid)
                .collection("game")
                .document(gameWishlist.id.toString())
                .delete()

            emit(ResponseResult.Success(task))
        } catch (e: Exception) {
            emit(ResponseResult.Error(e))
        }
    }

    @WorkerThread
    fun removeMovieFromWishlist(uid: String, movieWishlist: MovieWishlist) = flow {
        try {
            val task = database.collection(FIREBASE_COLLECTION_NAME)
                .document(uid)
                .collection("movie")
                .document(movieWishlist.id.toString())
                .delete()

            emit(ResponseResult.Success(task))
        } catch (e: Exception) {
            emit(ResponseResult.Error(e))
        }
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

    suspend fun getAllMovieWishlist(uid: String): QuerySnapshot? {
        return database.collection(FIREBASE_COLLECTION_NAME)
            .document(uid)
            .collection("movie")
            .get()
            .await()
    }
}