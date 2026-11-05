package com.example

import com.example.engine.VideoAnalyzer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testPlatformDetection() {
    assertEquals("YouTube", VideoAnalyzer.detectPlatform("https://www.youtube.com/watch?v=Cyberpunk2077"))
    assertEquals("TikTok", VideoAnalyzer.detectPlatform("https://www.tiktok.com/@creator/video/12345"))
    assertEquals("Instagram", VideoAnalyzer.detectPlatform("https://www.instagram.com/reel/abc123xyz"))
    assertEquals("Facebook", VideoAnalyzer.detectPlatform("https://www.facebook.com/watch/?v=987654"))
    assertEquals("Twitter", VideoAnalyzer.detectPlatform("https://twitter.com/NextGenRobotics/status/178293"))
    assertEquals("Web Video", VideoAnalyzer.detectPlatform("https://example.com/stream.mp4"))
  }

  @Test
  fun testMediaAnalysisFormats() {
    val info = VideoAnalyzer.analyze("https://www.youtube.com/watch?v=sample")
    assertNotNull(info)
    assertTrue(info.formats.isNotEmpty())
    assertTrue(info.formats.any { it.resolutionLabel == "1080p Full HD" })
    assertTrue(info.formats.any { it.formatType == "AUDIO" })
  }
}
