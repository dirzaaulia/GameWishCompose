package com.dirzaaulia.gamewish.utils

object MyAnimeListConstant {
    const val MYANIMELIST_URL = "myanimelist.net"
    const val MYANIMELIST_BASE_URL = "https://myanimelist.net"
    const val MYANIMELIST_API_URL = "https://api.myanimelist.net"
    const val MYANIMELIST_CLIENT_ID = "fb4e767cff574de2df92708d060a323c"
    const val MYANIMELIST_CODE_CHALLENGE =
        "u2Rs-9MLEb-eah-avSTbv8qrxVFLwpEfPwhBwsMAuXOTn_D0mlDFhMwPNVSQkLdZE70cpn5IuJW" +
                "_6vfkSOMZA36zAZ8b-BTmgokAjx-ecGDf1ddZ_0b5gg_SQsTauMdJ"
    const val MYANIMELIST_STATE = "gameWishRequest"
    const val MYANIMELIST_BASE_URL_CALLBACK = "https://dirzaaulia.com/callback"
    const val MYANIMELIST_TYPE_ANIME = "Anime"
    const val MYANIMELIST_TYPE_MANGA = "Manga"
    const val MYANIMELIST_STATUS = "Status"
    const val MYANIMELIST_STATUS_ALL = "All"
    const val MYANIMELIST_STATUS_WATCHING = "Watching"
    const val MYANIMELIST_STATUS_READING = "Reading"
    const val MYANIMELIST_STATUS_COMPLETED = "Completed"
    const val MYANIMELIST_STATUS_ON_HOLD = "On Hold"
    const val MYANIMELIST_STATUS_DROPPED = "Dropped"
    const val MYANIMELIST_SCORE_1 = "(1) - Appaling"
    const val MYANIMELIST_SCORE_2 = "(2) - Horrible"
    const val MYANIMELIST_SCORE_3 = "(3) - Very Bad"
    const val MYANIMELIST_SCORE_4 = "(4) - Bad"
    const val MYANIMELIST_SCORE_5 = "(5) - Average"
    const val MYANIMELIST_SCORE_6 = "(6) - Fine"
    const val MYANIMELIST_SCORE_7 = "(7) - Good"
    const val MYANIMELIST_SCORE_8 = "(8) - Very Good"
    const val MYANIMELIST_SCORE_9 = "(9) - Great"
    const val MYANIMELIST_SCORE_10 = "(10) - Masterpiece"
    const val MYANIMELIST_STATUS_PLAN_TO_WATCH = "Plan To Watch"
    const val MYANIMELIST_STATUS_PLAN_TO_READ = "Plan To Read"
    const val MYANIMELIST_WATCHED = "Watched"
    const val MYANIMELIST_READ = "Read"
    const val MYANIMELIST_IS_REWATCHING = "Is Rewatching"
    const val MYANIMELIST_IS_REREADING = "Is Rereading"
    const val MYANIMELIST_WEBVIEW_WISHLIST = 0
    const val MYANIMELIST_PAGE_SIZE_TEN = 10
    const val MYANIMELIST_DATA_NOT_FOUND = "Data Not Found"
    const val MYANIMELIST_GENERAL_ERROR = "Something went wrong when getting data from MyAnimeList. Please try it again later!"
    const val MYANIMELIST_BEARER_FORMAT = "Bearer %s"
    const val MYANIMELIST_GRANT_TYPE_REFRESH_TOKEN = "refresh_token"
    const val MYANIMELIST_SEASON_WINTER = "winter"
    const val MYANIMELIST_SEASON_SPRING = "spring"
    const val MYANIMELIST_SEASON_SUMMER = "summer"
    const val MYANIMELIST_SEASON_FALL = "fall"
    const val MYANIMELIST_EPISODES = "Episodes"
    const val MYANIMELIST_CHAPTERS = "Chapters"
    const val MYANIMELIST_SCORE = "Score"
    const val MYANIMELIST_RANK = "Rank"
    const val MYANIMELIST_POPULARITY = "Popularity"
    const val MYANIMELIST_MEMBERS = "Members"
    const val MYANIMELIST_SOURCE = "Source"
    const val MYANIMELIST_SYNOPSIS = "Synopsis"
    const val MYANIMELIST_BACKGROUND = "Background"
    const val MYANIMELIST_ANIME_LIST_UPDATE = "This anime has been updated on your Anime list"
    const val MYANIMELIST_ANIME_LIST_ADD = "This anime has been added on your Anime list"
    const val MYANIMELIST_MANGA_LIST_UPDATE = "This manga has been updated on your Anime list"
    const val MYANIMELIST_MANGA_LIST_ADD = "This manga has been added on your Anime list"
    const val MYANIMELIST_ANIME_LIST_UPDATE_FAILED = "Something went wrong when updating your Anime list. Please try again"
    const val MYANIMELIST_MANGA_LIST_UPDATE_FAILED = "Something went wrong when updating your Manga list. Please try again"
    const val MYANIMELIST_ANIME_LIST_DELETE = "This anime has been deleted from your Anime list"
    const val MYANIMELIST_MANGA_LIST_DELETE = "This manga has been deleted from your Manga list"
    const val MYANIMELIST_UPDATE_LIST_ANIME = "Update Anime In List"
    const val MYANIMELIST_UPDATE_LIST_MANGA = "Update Manga In List"
    const val MYANIMELIST_ADD_LIST_ANIME = "Add Anime To List"
    const val MYANIMELIST_ADD_LIST_MANGA = "Add Manga To List"
    const val MYANIMELIST_ACCOUNT = "MyAnimeList Account"
}

object DatabaseConstant {
    const val DATABASE_NAME = "gamewish_database"
    const val DATABASE_PAGING_SIZE = 10
}

object TmdbConstant {
    const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    const val TMDB_KEY = "2fce7a56fdfd95647be5e0d638d81da9"
    const val TMDB_BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original"
    const val TMBD_STATUS_ALL = "All"
    const val TMDB_TYPE_MOVIE = "Movie"
    const val TMDB_TYPE_TVSHOW = "TV Show"
    const val TMDB_PAGE_SIZE_TEN = 10
    const val TMDB_MOVIE_LIST_UPDATED = "This Movie has been updated on your Watchlist"
    const val TMDB_TV_LIST_UPDATED = "This TV Show has been updated on your Watchlist"
    const val TMDB_MOVIE_LIST_ADDED  = "This Movie has been added to your Watchlist"
    const val TMDB_TV_LIST_ADDED = "This TV Show has been added to your Watchlist"
    const val TMDB_MOVIE_LIST_UPDATE_ERROR = "Something wrong happen went updating Movie on your Watchlist"
    const val TMDB_TV_LIST_UPDATE_ERROR = "Something wrong happen went updating TV Show on your Watchlist"
    const val TMDB_MOVIE_LIST_ADD_ERROR = "Something wrong happen went adding Movie into your Watchlist"
    const val TMDB_TV_LIST_ADD_ERROR = "Something wrong happen went updating TV Show on your Watchlist"
    const val TMDB_MOVIE_LIST_DELETED =  "This Movie has been deleted from your Watchlist"
    const val TMDB_TV_LIST_DELETED = "This TV Show has been deleted from your Watchlist"
    const val TMDB_MOVIE_LIST_DELETE_ERROR = "Something wrong happen when deleting Movie from your Watchlist"
    const val TMDB_TV_LIST_DELETE_ERROR = "Something wrong happen when deleting TV Show from your Watchlist"
    const val TMDB_UPDATE_MOVIE = "Update Movie in your Watchlist"
    const val TMDB_UPDATE_TV = "Update TV Show in your Watchlist"
    const val TMDB_ADD_MOVIE = "Add Movie into your Watchlist"
    const val TMDB_ADD_TV = "Add TV Show into your Watchlist"
    const val TMDB_SCORE = "Score"
    const val TMDB_POPULARITY = "Popularity"
    const val TMDB_PRODUCTION_COMAPNIES = "Production Companies"
    const val TMDB_BUDGET = "Budget"
    const val TMDB_REVENUE = "Revenue"
    const val TMDB_SEASONS = "Seasons"
    const val TMDB_EPISODES = "Episodes"
    const val TMDB_STATUS_PLAN_TO_WATCH = "Plan To Watch"
    const val TMDB_STATUS_WATCHING = "Watching"
    const val TMDB_STATUS_COMPLETED = "Completed"
    const val TMDB_STATUS_ON_HOLD = "On-Hold"
    const val TMDB_STATUS_DROPPED = "Dropped"
    const val TMDB_STATUS = "Status"
}

object RawgConstant {
    const val RAWG_BASE_URL = "https://api.rawg.io/api/"
    const val RAWG_KEY = "b1ece946fa424f4f887871a867743d0e"
    const val RAWG_PAGE_SIZE_TEN = 10
    const val RAWG_PAGE_SIZE_FIFTY = 50
    const val RAWG_WISHLIST_UPDATED = "This game has been updated on your Wishlist."
    const val RAWG_WISHLIST_ADDED = "This game has been added to your Wishlist."
    const val RAWG_WISHLIST_UPDATE_ERROR = "Something wrong happen went updating game in your Wishlist."
    const val RAWG_WISHLIST_ADD_ERROR = "Something wrong happen went updating game in your Wishlist."
    const val RAWG_WISHLIST_DELETED = "This game has been deleted from your Wishlist."
    const val RAWG_WISHLIST_DELETE_ERROR = "Something went wrong when deleting game from your Wishlist."
    const val RAWG_STATUS = "Status"
    const val RAWG_STATUS_PLAN_TO_BUY = "Plan To Buy"
    const val RAWG_STATUS_PLAYING = "Playing"
    const val RAWG_STATUS_COMPLETED = "Completed"
    const val RAWG_STATUS_ON_HOLD = "On Hold"
    const val RAWG_STATUS_DROPPED = "Dropped"
    const val RAWG_UPDATE_WISHLIST = "Update Wishlist"
    const val RAWG_ADD_WISHLIST = "Add To Wishlist"
}

object CheapSharkConstant {
    const val CHEAPSHARK_BASE_URL = "https://www.cheapshark.com/api/1.0/"
    const val CHEAPSHARK_URL = "https://www.cheapshark.com/redirect?dealID=%s"
    const val CHEAPSHARK_STORE = "Store"
    const val CHEAPSHARK_DEFAULT_STORE_NAME = "Steam"
    const val CHEAPSHARK_DEFAULT_STORE_ID = "1"
    const val CHEAPSHARK_DEFAULT_LOWER_PRICE = 0L
    const val CHEAPSHARK_DEFAULT_UPPER_PRICE = 1000L
    const val CHEAPSHARK_PAGE_SIZE_TEN = 10
}

object ProtoConstant {
    const val DATA_STORE_FILE_NAME = "user_prefs.pb"
}

object FirebaseConstant {
    const val GOOGLE_SIGN_IN_WEB_CLIENT_ID =
        "203885987594-4u6tk8rbssjram2c5jf5jsls5ejeig0s.apps.googleusercontent.com"
    const val FIREBASE_DATABASE_URL =
        "https://gamewish-f7e0c-default-rtdb.asia-southeast1.firebasedatabase.app/"
    const val FIREBASE_COLLECTION_WISHLIST = "wishlist"
    const val FIREBASE_COLLECTION_GAME = "game"
    const val FIREBASE_COLLECTION_MOVIE = "movie"
}

object PlaceholderConstant {
    const val DEFAULT = -1
    const val GAME_WISHLIST = 0
    const val MOVIE_WISHLIST = 1
    const val DEALS = 2
    const val SEARCH_GAME_TAB = 3
    const val SEARCH_GAME = 4
    const val ANIME = 5
}

object PlatformsConstant {
    const val XBOX = "Xbox"
    const val ANDROID = "Android"
    const val PLAYSTATION = "Playstation"
    const val PS = "PS"
    const val NINTENDO = "Nintendo"
    const val WII = "Wii"
    const val NES = "NES"
}

object OtherConstant {

    const val ZERO = 0
    const val ONE = 1
    const val TWO = 2
    const val THREE = 3
    const val FOUR = 4
    const val FIVE = 5
    const val SIX = 6
    const val EIGHT = 8
    const val NINE = 9
    const val TEN = 10
    const val ELEVEN = 11
    const val SIX_HUNDRED = 600

    const val ZERO_LONG = 0L
    const val THIRTY_LONG = 30L
    const val TWO_THOUSAND_LONG = 2000L

    const val STRIP = "-"
    const val UNDERSCORE = "_"
    const val EMPTY_STRING = ""
    const val BLANK_SPACE = " "
    const val HASHTAG = "#"
    const val COLON = ":"

    const val DATE_FORMAT_STRIP_dd_MM_yyyy = "dd-MM-yyyy"
    const val DATE_FORMAT_STRIP_yyyy_MM_dd = "yyyy-MM-dd"
    const val DATE_FORMAT_STRIP_yyyy_MM = "yyyy-MM"

    const val STRING_FORMAT_S_S = "%s%s"
    const val STRING_FORMAT_S_SPACE_S_SPACE_S = "%s %s %s"
    const val STRING_FORMAT_S_SPACE_S = "%s %s"
    const val STRING_FORMAT_S_STRIP_S = "%s - %s"

    const val NOMINAL_FORMAT = "%,d"

    const val NO_IMAGE_URL =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/No-Image-Placeholder.svg/1200px-No-Image-Placeholder.svg.png"
    const val LOCAL_IMAGES_ERROR = "No LocalImages specified"
    const val NOT_FOUND_EXCEPTION = "Data Not Found"
    const val HTTP_ERROR_401 = "HTTP 401"
    const val GOOGLE_ACCOUNT = "Google Account"
    const val LOGOUT_ACCOUNT = "Logout Account"
    const val UNLINK_ACOCUNT = "Unlink Account"
    const val LINK_ACCOUNT = "Link Account"
    const val ABOUT_GAMEWISH = "About GameWish"
    const val WEBSITE = "https://dirzaaulia.com"
    const val PLAY_STORE_LINK = "https://play.google.com/store/apps/dev?id=4806849608818858118"
    const val DATA_SOURCE = "Data Source"
    const val RAWG_LINK = "https://www.rawg.io"
    const val CHEAPSHARK_LINK = "https://www.cheapshark.com/"
    const val MYANIMELIST_LINK = "https://wwww.myanimelist.net/"
    const val TMDB_LINK = "https://wwww.themoviedb.org/"
    const val NOW = "Now"
    const val ALL = "All"
}

object Route {
    const val LOGIN = "Login"
    const val MYANIMELIST_LOGIN = "MyAnimeListLogin"
    const val HOME = "Home"
    const val SEARCH = "Search"
    const val SEARCH_ARGS = "Search/{menuId}"
    const val SEARCH_ARG_0 = "menuId"
    const val GAME_DETAILS = "GameDetails"
    const val GAME_DETAILS_ARGS = "GameDetails/{gameId}"
    const val GAME_DETAILS_ARG_0 = "gameId"
    const val ANIME_DETAILS = "AnimeDetails"
    const val ANIME_DETAILS_ARGS = "AnimeDetails/{animeId}/{type}"
    const val ANIME_DETAILS_ARG_0 = "animeId"
    const val ANIME_DETAILS_ARG_1 = "type"
    const val MOVIE_DETAILS = "MovieDetails"
    const val MOVIE_DETAILS_ARGS = "MovieDetails/{movieId}/{type}"
    const val MOVIE_DETAILS_ARG_0 = "movieId"
    const val MOVIE_DETAILS_ARG_1 = "type"
}