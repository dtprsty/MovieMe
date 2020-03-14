package id.dtprsty.movieme.feature

import id.dtprsty.movieme.data.remote.movie.Movie

interface IRecyclerView {

    fun onClick(movie: Movie)
}