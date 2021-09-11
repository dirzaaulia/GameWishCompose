package com.dirzaaulia.gamewish.network.firebase

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.repository.FirebaseRepository
import com.dirzaaulia.gamewish.utils.FirebaseConstant.FIREBASE_COLLECTION_NAME
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebasePagingSource(
    private val firebaseRepository: FirebaseRepository,
    private val uid: String,
    private var gameStatus: String
) : PagingSource<QuerySnapshot, Wishlist>() {

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Wishlist>): QuerySnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Wishlist> {
        return try {
            val database = firebaseRepository.getFirestore()

            // Step 1
            val currentPage = if (gameStatus.isBlank()) {
                params.key ?: database.collection(FIREBASE_COLLECTION_NAME)
                    .document(uid)
                    .collection("game")
                    .limit(10)
                    .get()
                    .addOnSuccessListener { Timber.i("${it.documents.size}") }
                    .addOnFailureListener { Timber.i(it.message.toString()) }
                    .await()
            } else {
                params.key ?: database.collection(FIREBASE_COLLECTION_NAME)
                    .document(uid)
                    .collection("game")
                    .whereEqualTo("status", gameStatus)
                    .limit(10)
                    .get()
                    .addOnSuccessListener { Timber.i("${it.documents.size}") }
                    .addOnFailureListener { Timber.i(it.message.toString()) }
                    .await()
            }

            // Step 2
            val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]

            // Step 3
            val nextPage = if (gameStatus.isBlank()) {
                database.collection(FIREBASE_COLLECTION_NAME)
                    .document(uid)
                    .collection("game")
                    .limit(10)
                    .startAfter(lastDocumentSnapshot)
                    .get()
                    .await()
            } else {
                database.collection(FIREBASE_COLLECTION_NAME)
                    .document(uid)
                    .collection("game")
                    .whereEqualTo("status", gameStatus)
                    .limit(10)
                    .startAfter(lastDocumentSnapshot)
                    .get()
                    .await()
            }

            // Step 4
            if (currentPage.isEmpty) {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            } else {
                LoadResult.Page(
                    data = currentPage.toObjects(Wishlist::class.java),
                    prevKey = null,
                    nextKey = nextPage
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}