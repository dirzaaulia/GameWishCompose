package com.dirzaaulia.gamewish.repository

import androidx.annotation.WorkerThread
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.base.executeFirebase
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import com.dirzaaulia.gamewish.data.model.wishlist.MovieWishlist
import com.dirzaaulia.gamewish.utils.FirebaseConstant.FIREBASE_COLLECTION_GAME
import com.dirzaaulia.gamewish.utils.FirebaseConstant.FIREBASE_COLLECTION_MOVIE
import com.dirzaaulia.gamewish.utils.FirebaseConstant.FIREBASE_COLLECTION_WISHLIST
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseRepository {

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
        emit(ResponseResult.Loading)
        emit(
            executeFirebase {
                database.collection(FIREBASE_COLLECTION_WISHLIST)
                    .document(uid)
                    .collection(FIREBASE_COLLECTION_GAME)
                    .document(gameWishlist.id.toString())
                    .set(gameWishlist)
            }
        )
    }

    @WorkerThread
    fun addMovieToWishlist(uid: String, movieWishlist: MovieWishlist) = flow {
        emit(ResponseResult.Loading)
        emit(
            executeFirebase {
                database.collection(FIREBASE_COLLECTION_WISHLIST)
                    .document(uid)
                    .collection(FIREBASE_COLLECTION_MOVIE)
                    .document(movieWishlist.id.toString())
                    .set(movieWishlist)
            }
        )
    }

    @WorkerThread
    fun removeGameFromWishlist(uid: String, gameWishlist: GameWishlist) = flow {
        emit(ResponseResult.Loading)
        emit(
            executeFirebase {
                database.collection(FIREBASE_COLLECTION_WISHLIST)
                    .document(uid)
                    .collection(FIREBASE_COLLECTION_GAME)
                    .document(gameWishlist.id.toString())
                    .delete()
            }
        )
    }

    @WorkerThread
    fun removeMovieFromWishlist(uid: String, movieWishlist: MovieWishlist) = flow {
        emit(ResponseResult.Loading)
        emit(
            executeFirebase {
                database.collection(FIREBASE_COLLECTION_WISHLIST)
                    .document(uid)
                    .collection(FIREBASE_COLLECTION_MOVIE)
                    .document(movieWishlist.id.toString())
                    .delete()
            }
        )
    }

    suspend fun getAllGameWishlist(uid: String): QuerySnapshot? {
        return database.collection(FIREBASE_COLLECTION_WISHLIST)
            .document(uid)
            .collection(FIREBASE_COLLECTION_GAME)
            .get()
            .await()
    }

    suspend fun getAllMovieWishlist(uid: String): QuerySnapshot? {
        return database.collection(FIREBASE_COLLECTION_WISHLIST)
            .document(uid)
            .collection(FIREBASE_COLLECTION_MOVIE)
            .get()
            .await()
    }
}