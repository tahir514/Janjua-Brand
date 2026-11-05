package com.example.engine

data class MediaFormatOption(
    val id: String,
    val resolutionLabel: String, // e.g. "1080p Full HD"
    val qualityTag: String, // e.g. "60 FPS", "Recommended", "HD", "Ultra", "HQ Audio"
    val extension: String, // "mp4", "mp3", "m4a"
    val formatType: String, // "VIDEO" or "AUDIO"
    val estimatedSizeBytes: Long,
    val estimatedSizeFormatted: String, // e.g. "45.2 MB"
    val directUrl: String = ""
)

data class AnalyzedMediaInfo(
    val title: String,
    val author: String,
    val platform: String,
    val duration: String,
    val views: String,
    val thumbnailUrl: String,
    val formats: List<MediaFormatOption>,
    val canRemoveWatermark: Boolean = true,
    val originalUrl: String
)

object VideoAnalyzer {

    fun detectPlatform(url: String): String {
        val lower = url.lowercase().trim()
        return when {
            lower.contains("youtube.com") || lower.contains("youtu.be") -> "YouTube"
            lower.contains("tiktok.com") -> "TikTok"
            lower.contains("instagram.com") -> "Instagram"
            lower.contains("facebook.com") || lower.contains("fb.watch") -> "Facebook"
            lower.contains("twitter.com") || lower.contains("x.com") -> "Twitter"
            lower.contains("vimeo.com") -> "Vimeo"
            lower.contains("pinterest.com") -> "Pinterest"
            else -> "Web Video"
        }
    }

    fun analyze(url: String): AnalyzedMediaInfo {
        val platform = detectPlatform(url)
        val cleanUrl = url.trim()

        // Reliable sample playable streams that genuinely download and play
        val sampleVideoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
        val sampleAudioUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"

        val (title, author, duration, views, thumb) = when (platform) {
            "YouTube" -> MediaMeta(
                title = if (cleanUrl.contains("watch") || cleanUrl.contains("youtu.be"))
                    "Cyberpunk 2077 Night City 4K Ultra Cinematic HDR"
                else "Trending Tech Showcase & AI Innovations 2026",
                author = "FutureTech Official",
                duration = "08:42",
                views = "2.4M views",
                thumbnail = "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800&q=80"
            )
            "TikTok" -> MediaMeta(
                title = "Insane cinematic drone glide through futuristic Tokyo neon streets",
                author = "@neon_explorer",
                duration = "00:45",
                views = "890K likes",
                thumbnail = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800&q=80"
            )
            "Instagram" -> MediaMeta(
                title = "Hyper-realistic VFX Breakdown & Color Grading Masterclass #Reels",
                author = "@motion_creatives",
                duration = "01:15",
                views = "450K views",
                thumbnail = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800&q=80"
            )
            "Facebook" -> MediaMeta(
                title = "Deep Sea Discovery: Bioluminescent Creatures in 4K HDR",
                author = "Earth Odyssey Watch",
                duration = "12:30",
                views = "1.8M views",
                thumbnail = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&q=80"
            )
            "Twitter" -> MediaMeta(
                title = "Next-Gen AI Robotics Autonomous Navigation Demo",
                author = "@NextGenRobotics",
                duration = "02:10",
                views = "620K views",
                thumbnail = "https://images.unsplash.com/photo-1485827404703-89b55fcc595e?w=800&q=80"
            )
            else -> MediaMeta(
                title = "High Speed Media Stream - Universal AI Video Extractor",
                author = "Universal Web Source",
                duration = "04:15",
                views = "Verified Stream",
                thumbnail = "https://images.unsplash.com/photo-1511512578047-dfb367046420?w=800&q=80"
            )
        }

        val formats = listOf(
            MediaFormatOption(
                id = "fmt_4k",
                resolutionLabel = "2160p 4K",
                qualityTag = "Ultra HD",
                extension = "mp4",
                formatType = "VIDEO",
                estimatedSizeBytes = 134_217_728L,
                estimatedSizeFormatted = "128.0 MB",
                directUrl = sampleVideoUrl
            ),
            MediaFormatOption(
                id = "fmt_2k",
                resolutionLabel = "1440p 2K",
                qualityTag = "Quad HD",
                extension = "mp4",
                formatType = "VIDEO",
                estimatedSizeBytes = 78_643_200L,
                estimatedSizeFormatted = "75.0 MB",
                directUrl = sampleVideoUrl
            ),
            MediaFormatOption(
                id = "fmt_1080p",
                resolutionLabel = "1080p Full HD",
                qualityTag = "Recommended 60fps",
                extension = "mp4",
                formatType = "VIDEO",
                estimatedSizeBytes = 47_185_920L,
                estimatedSizeFormatted = "45.0 MB",
                directUrl = sampleVideoUrl
            ),
            MediaFormatOption(
                id = "fmt_720p",
                resolutionLabel = "720p HD",
                qualityTag = "Fast Download",
                extension = "mp4",
                formatType = "VIDEO",
                estimatedSizeBytes = 23_592_960L,
                estimatedSizeFormatted = "22.5 MB",
                directUrl = sampleVideoUrl
            ),
            MediaFormatOption(
                id = "fmt_480p",
                resolutionLabel = "480p SD",
                qualityTag = "Data Saver",
                extension = "mp4",
                formatType = "VIDEO",
                estimatedSizeBytes = 12_582_912L,
                estimatedSizeFormatted = "12.0 MB",
                directUrl = sampleVideoUrl
            ),
            MediaFormatOption(
                id = "fmt_360p",
                resolutionLabel = "360p",
                qualityTag = "Standard",
                extension = "mp4",
                formatType = "VIDEO",
                estimatedSizeBytes = 7_340_032L,
                estimatedSizeFormatted = "7.0 MB",
                directUrl = sampleVideoUrl
            ),
            MediaFormatOption(
                id = "fmt_240p",
                resolutionLabel = "240p",
                qualityTag = "Lightweight",
                extension = "mp4",
                formatType = "VIDEO",
                estimatedSizeBytes = 4_194_304L,
                estimatedSizeFormatted = "4.0 MB",
                directUrl = sampleVideoUrl
            ),
            MediaFormatOption(
                id = "fmt_144p",
                resolutionLabel = "144p",
                qualityTag = "Ultra Saver",
                extension = "mp4",
                formatType = "VIDEO",
                estimatedSizeBytes = 2_097_152L,
                estimatedSizeFormatted = "2.0 MB",
                directUrl = sampleVideoUrl
            ),
            // Audio options
            MediaFormatOption(
                id = "fmt_mp3",
                resolutionLabel = "MP3 Audio",
                qualityTag = "320 kbps HQ",
                extension = "mp3",
                formatType = "AUDIO",
                estimatedSizeBytes = 7_864_320L,
                estimatedSizeFormatted = "7.5 MB",
                directUrl = sampleAudioUrl
            ),
            MediaFormatOption(
                id = "fmt_m4a",
                resolutionLabel = "M4A Audio",
                qualityTag = "AAC 256 kbps",
                extension = "m4a",
                formatType = "AUDIO",
                estimatedSizeBytes = 5_242_880L,
                estimatedSizeFormatted = "5.0 MB",
                directUrl = sampleAudioUrl
            ),
            MediaFormatOption(
                id = "fmt_orig_audio",
                resolutionLabel = "Original Audio",
                qualityTag = "Studio Quality",
                extension = "m4a",
                formatType = "AUDIO",
                estimatedSizeBytes = 3_932_160L,
                estimatedSizeFormatted = "3.8 MB",
                directUrl = sampleAudioUrl
            )
        )

        return AnalyzedMediaInfo(
            title = title,
            author = author,
            platform = platform,
            duration = duration,
            views = views,
            thumbnailUrl = thumb,
            formats = formats,
            canRemoveWatermark = (platform == "TikTok" || platform == "Instagram"),
            originalUrl = cleanUrl
        )
    }

    private data class MediaMeta(
        val title: String,
        val author: String,
        val duration: String,
        val views: String,
        val thumbnail: String
    )
}
