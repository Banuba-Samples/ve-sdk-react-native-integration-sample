package com.vesdkreactnativeintegrationsample.videoeditor.export

import android.net.Uri
import androidx.core.net.toFile
import com.vesdkreactnativeintegrationsample.videoeditor.export.ExportVideoResolutionProvider
import com.banuba.sdk.core.VideoResolution
import com.banuba.sdk.core.media.MediaFileNameHelper.Companion.DEFAULT_SOUND_FORMAT
import com.banuba.sdk.export.data.ExportParamsProvider
import com.banuba.sdk.ve.domain.VideoRangeList
import com.banuba.sdk.ve.effects.Effects
import com.banuba.sdk.ve.effects.watermark.WatermarkAlignment
import com.banuba.sdk.ve.effects.watermark.WatermarkBuilder
import com.banuba.sdk.ve.effects.music.MusicEffect
import com.banuba.sdk.ve.ext.withWatermark
import com.banuba.sdk.export.data.ExportParams

class IntegrationAppExportParamsProvider(
    private val exportDir: Uri,
    private val sizeProvider: ExportVideoResolutionProvider,
    private val watermarkBuilder: WatermarkBuilder
) : ExportParamsProvider {

    override fun provideExportParams(
        effects: Effects,
        videoRangeList: VideoRangeList,
        musicEffects: List<MusicEffect>,
        videoVolume: Float
    ): List<ExportParams> {
        val exportSessionDir = exportDir.toFile().apply {
            deleteRecursively()
            mkdirs()
        }
        val extraSoundtrackUri = Uri.parse(exportSessionDir.toString()).buildUpon()
            .appendPath("exported_soundtrack.$DEFAULT_SOUND_FORMAT")
            .build()

        return listOf(
            ExportParams.Builder(sizeProvider.videoResolution)
                .effects(effects.withWatermark(watermarkBuilder, WatermarkAlignment.BOTTOM_RIGHT))
                .fileName("export_default_watermark")
                .videoRangeList(videoRangeList)
                .destDir(exportSessionDir)
                .musicEffects(musicEffects)
                .extraAudioFile(extraSoundtrackUri)
                .volumeVideo(videoVolume)
                .build()
        )
    }
}