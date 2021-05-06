package com.example.midterm

class MusicList {
    companion object {
        val dataSet = listOf(
            Music(
                1,
                "Back in Black",
                "AC-DC",
                R.raw.back_in_black,
                R.raw.back_in_black_lyrics,
                R.drawable.back_in_black_poster
            ),
            Music(
                2,
                "Fire on fire",
                "Sam Smith",
                R.raw.fire_on_fire,
                R.raw.fire_on_fire_lyrics,
                R.drawable.fire_on_fire_poster
            ),
            Music(
                3,
                "Levitating",
                "Dua Lipa",
                R.raw.levitating,
                R.raw.levitating_lyrics,
                R.drawable.levitating_poster
            ),
            Music(
                4,
                "Spirits",
                "The Strumbellas",
                R.raw.spirits,
                R.raw.spirits_lyrics,
                R.drawable.spirits_poster
            ),
            Music(
                5,
                "Мистер 718",
                "Скриптонит",
                R.raw.mister_718,
                R.raw.mister_718_lyrics,
                R.drawable.mister_718_poster
            )
        )
    }
}