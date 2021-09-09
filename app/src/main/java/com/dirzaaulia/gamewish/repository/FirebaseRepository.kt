package com.dirzaaulia.gamewish.repository

import androidx.annotation.WorkerThread
import com.dirzaaulia.gamewish.base.NotFoundException
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.utils.FirebaseConstant.FIREBASE_DATABASE_URL
import com.dirzaaulia.gamewish.utils.FirebaseConstant.FIREBASE_TABLE_NAME
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseRepository {

    private val auth = Firebase.auth
    private val realtimeDatabase = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL)

    fun getFirebaseAuth(): FirebaseAuth {
        return auth
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

    fun getAllWishlistFromRealtimeDatabase(uid: String): DatabaseReference {
        return realtimeDatabase.reference.child(FIREBASE_TABLE_NAME).child(uid)
    }

    fun getWishlistFromRealtimeDatabase(uid: String, gameId: String): DatabaseReference {
        return realtimeDatabase.reference.child(FIREBASE_TABLE_NAME).child(uid).child(gameId)
    }

    fun addWishlistToRealtimeDatabase(uid: String, wishlist: Wishlist) {
        realtimeDatabase.reference
            .child(FIREBASE_TABLE_NAME)
            .child(uid)
            .child(wishlist.id.toString())
            .setValue(
                wishlist, DatabaseReference.CompletionListener { error, ref ->
                    if (error != null) {
                        Timber.w(
                            "Unable to write wishlist to database : %s",
                            error.toException().toString()
                        )
                        return@CompletionListener
                    } else {
                        Timber.i("Database Reference : %s", ref.toString())
                    }
                })
    }

    fun removeWishlistFromRealtimeDatabase(uid: String, wishlist: Wishlist) {
        realtimeDatabase.reference
            .child(FIREBASE_TABLE_NAME)
            .child(uid)
            .child(wishlist.id.toString())
            .removeValue()
    }
}