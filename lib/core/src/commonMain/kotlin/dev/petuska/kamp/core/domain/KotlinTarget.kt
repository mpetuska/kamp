package dev.petuska.kamp.core.domain

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

@Serializable(with = KotlinTarget.Serializer::class)
sealed class KotlinTarget(
  val category: String,
  val platform: String,
) {
  object Serializer : KSerializer<KotlinTarget> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("KotlinTarget") {
      element<String>("category")
      element<String>("platform")
    }

    override fun deserialize(decoder: Decoder): KotlinTarget {
      return decoder.decodeStructure(descriptor) {
        var category: String? = null
        var platform: String? = null
        while (true) {
          when (val index = decodeElementIndex(descriptor)) {
            0 -> category = decodeStringElement(descriptor, 0)
            1 -> platform = decodeStringElement(descriptor, 1)
            CompositeDecoder.DECODE_DONE -> break
            else -> error("Unexpected index: $index")
          }
        }
        requireNotNull(category)
        requireNotNull(platform)
        values().find { it.platform == platform && it.category == category } ?: Unknown(platform)
      }
    }

    override fun serialize(encoder: Encoder, value: KotlinTarget) {
      encoder.encodeStructure(descriptor) {
        encodeStringElement(descriptor, 0, value.category)
        encodeStringElement(descriptor, 1, value.platform)
      }
    }
  }

  override fun toString(): String = "$category:$platform"
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is KotlinTarget) return false

    if (platform != other.platform) return false
    if (category != other.category) return false

    return true
  }

  override fun hashCode(): Int {
    var result = platform.hashCode()
    result = 31 * result + category.hashCode()
    return result
  }

  companion object {
    fun values(): Set<KotlinTarget> = JS.values() + JVM.values() + Native.values() + Common
  }

  class Unknown(platform: String) : KotlinTarget("unknown", platform)

  object Common : KotlinTarget("metadata", "common")

  sealed class JS(platform: String) : KotlinTarget(CATEGORY, platform) {
    companion object {
      const val CATEGORY = "js"
      fun values(): Set<JS> = setOf(Legacy, IR)
    }

    object Legacy : JS("legacy")

    object IR : JS("ir")
  }

  sealed class JVM(platform: String) : KotlinTarget(CATEGORY, platform) {
    companion object {
      const val CATEGORY = "jvm"
      fun values(): Set<JVM> = setOf(Java, Android)
    }

    object Java : JVM("java")

    object Android : JVM("android")
  }

  sealed class Native(val family: String, platform: String) : KotlinTarget(CATEGORY, platform) {
    companion object {
      const val CATEGORY = "native"
      fun values(): Set<Native> = AndroidNative.values() +
        IOS.values() +
        WatchOS.values() +
        TvOS.values() +
        MacOS.values() +
        Mingw.values() +
        Linux.values() +
        Wasm32
    }

    object Wasm32 : Native("wasm", "wasm32")

    sealed class AndroidNative(val architecture: String) : Native(FAMILY, "$FAMILY$architecture") {
      companion object {
        const val FAMILY = "androidNative"
        fun values(): Set<AndroidNative> = setOf(AndroidNativeArm32, AndroidNativeArm64)
      }

      object AndroidNativeArm32 : AndroidNative("Arm32")

      object AndroidNativeArm64 : AndroidNative("Arm64")
    }

    sealed class IOS(val architecture: String) : Native(FAMILY, "$FAMILY$architecture") {
      companion object {
        const val FAMILY = "ios"
        fun values(): Set<IOS> = setOf(
          IOSArm32, IOSArm64, IOSX64, IOSSimulatorArm64
        )
      }

      object IOSArm32 : IOS("Arm32")

      object IOSArm64 : IOS("Arm64")

      object IOSX64 : IOS("X64")

      object IOSSimulatorArm64 : IOS("SimulatorArm64")
    }

    sealed class WatchOS(val architecture: String) : Native(FAMILY, "$FAMILY$architecture") {
      companion object {
        const val FAMILY = "watchos"
        fun values(): Set<WatchOS> = setOf(
          WatchOSX86, WatchOSX64, WatchOSArm64, WatchOSArm32, WatchOSSimulatorArm64
        )
      }

      object WatchOSX86 : WatchOS("X86")

      object WatchOSX64 : WatchOS("X64")

      object WatchOSArm64 : WatchOS("Arm64")

      object WatchOSArm32 : WatchOS("Arm32")

      object WatchOSSimulatorArm64 : WatchOS("SimulatorArm64")
    }

    sealed class TvOS(val architecture: String) : Native(FAMILY, "$FAMILY$architecture") {
      companion object {
        const val FAMILY = "tvos"
        fun values(): Set<TvOS> = setOf(
          TvOSArm64, TvOSX64, TvOSSimulatorArm64
        )
      }

      object TvOSArm64 : TvOS("Arm64")

      object TvOSX64 : TvOS("X64")

      object TvOSSimulatorArm64 : TvOS("SimulatorArm64")
    }

    sealed class MacOS(val architecture: String) : Native(FAMILY, "$FAMILY$architecture") {
      companion object {
        const val FAMILY = "macos"
        fun values(): Set<MacOS> = setOf(
          MacOSArm64, MacOSX64
        )
      }

      object MacOSArm64 : MacOS("Arm64")

      object MacOSX64 : MacOS("X64")
    }

    sealed class Mingw(val architecture: String) : Native(FAMILY, "$FAMILY$architecture") {
      companion object {
        const val FAMILY = "mingw"
        fun values(): Set<Mingw> = setOf(
          MingwX64, MingwX86
        )
      }

      object MingwX64 : Mingw("X64")

      object MingwX86 : Mingw("X86")
    }

    sealed class Linux(val architecture: String) : Native(FAMILY, "$FAMILY$architecture") {
      companion object {
        const val FAMILY = "linux"
        fun values(): Set<Linux> = setOf(
          LinuxArm32Hfp, LinuxMips32, LinuxMipsel32, LinuxX64, LinuxArm64,
        )
      }

      object LinuxArm32Hfp : Linux("Arm32Hfp")

      object LinuxMips32 : Linux("Mips32")

      object LinuxMipsel32 : Linux("Mipsel32")

      object LinuxX64 : Linux("X64")

      object LinuxArm64 : Linux("Arm64")
    }
  }
}
