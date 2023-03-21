package com.dirzaaulia.gamewish.data.model.wishlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.RawgConstant
import com.dirzaaulia.gamewish.utils.TmdbConstant

enum class FilterDialogType {
    GAME,
    ANIME,
    MANGA,
    MOVIE,
    TV;

    companion object {
        fun FilterDialogType.setStatus(status: String): String {
            return when (this) {
                GAME -> status.ifBlank { RawgConstant.RAWG_STATUS_ALL }
                else -> status
            }
        }

        fun FilterDialogType.getFilterList(): List<String> {
            return when (this) {
                GAME -> listOf(
                    RawgConstant.RAWG_STATUS_ALL,
                    RawgConstant.RAWG_STATUS_PLAYING,
                    RawgConstant.RAWG_STATUS_COMPLETED,
                    RawgConstant.RAWG_STATUS_ON_HOLD,
                    RawgConstant.RAWG_STATUS_DROPPED,
                    RawgConstant.RAWG_STATUS_PLAN_TO_BUY
                )
                ANIME -> listOf(
                    MyAnimeListConstant.MYANIMELIST_STATUS_ALL,
                    MyAnimeListConstant.MYANIMELIST_STATUS_WATCHING,
                    MyAnimeListConstant.MYANIMELIST_STATUS_COMPLETED,
                    MyAnimeListConstant.MYANIMELIST_STATUS_ON_HOLD,
                    MyAnimeListConstant.MYANIMELIST_STATUS_DROPPED,
                    MyAnimeListConstant.MYANIMELIST_STATUS_PLAN_TO_WATCH
                )
                MANGA -> listOf(
                    MyAnimeListConstant.MYANIMELIST_STATUS_ALL,
                    MyAnimeListConstant.MYANIMELIST_STATUS_READING,
                    MyAnimeListConstant.MYANIMELIST_STATUS_COMPLETED,
                    MyAnimeListConstant.MYANIMELIST_STATUS_ON_HOLD,
                    MyAnimeListConstant.MYANIMELIST_STATUS_DROPPED,
                    MyAnimeListConstant.MYANIMELIST_STATUS_PLAN_TO_READ
                )
                MOVIE, TV -> listOf(
                    TmdbConstant.TMBD_STATUS_ALL,
                    TmdbConstant.TMDB_STATUS_WATCHING,
                    TmdbConstant.TMDB_STATUS_COMPLETED,
                    TmdbConstant.TMDB_STATUS_ON_HOLD,
                    TmdbConstant.TMDB_STATUS_DROPPED,
                    TmdbConstant.TMDB_STATUS_PLAN_TO_WATCH
                )
            }
        }

        @Composable
        fun FilterDialogType.setFilterTitle(): String {
            return when (this) {
                GAME -> stringResource(R.string.filter_game)
                MOVIE -> stringResource(R.string.filter_movie)
                TV -> stringResource(R.string.filter_tv)
                else -> OtherConstant.EMPTY_STRING
            }
        }

        @Composable
        fun FilterDialogType.setFilterName(): String {
            return when (this) {
                GAME -> stringResource(R.string.game_name)
                MOVIE -> stringResource(R.string.movie_name)
                TV -> stringResource(R.string.tv_name)
                else -> OtherConstant.EMPTY_STRING
            }
        }

        @Composable
        fun FilterDialogType.setSortTitle(): String {
            return when (this) {
                GAME -> stringResource(R.string.sort_game)
                ANIME -> stringResource(R.string.sort_anime)
                MANGA -> stringResource(R.string.sort_manga)
                MOVIE -> stringResource(R.string.sort_movie)
                TV -> stringResource(R.string.sort_tv)
            }
        }

        fun FilterDialogType.doSort(
            viewModel: HomeViewModel,
            status: String
        ) {
            return when (this) {
                GAME -> viewModel.setGameStatus(status)
                ANIME -> viewModel.setAnimeStatus(status)
                MANGA -> viewModel.setMangaStatus(status)
                MOVIE -> viewModel.setMovieStatus(status)
                TV -> viewModel.setTVShowStatus(status)
            }
        }
    }
}