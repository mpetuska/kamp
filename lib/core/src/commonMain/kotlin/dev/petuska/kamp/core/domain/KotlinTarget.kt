package dev.petuska.kamp.core.domain

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*

@Suppress("CanBeParameter", "MemberVisibilityCanBePrivate")
@Serializable(with = KotlinTarget.Serializer::class)
sealed class KotlinTarget(
  val category: String,
  val family: String = category,
  val platform: String,
  val displayCategory: String = category,
  val displayPlatform: String = platform,
  val id: String = platform,
) {
  object Serializer : KSerializer<KotlinTarget> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("KotlinTarget") {
      element<String>("id")
      element<String>("category")
      element<String>("family")
      element<String>("platform")
    }

    override fun deserialize(decoder: Decoder): KotlinTarget {
      return decoder.decodeStructure(descriptor) {
        var category: String? = null
        var platform: String? = null
        var id: String? = null
        while (true) {
          when (val index = decodeElementIndex(descriptor)) {
            0 -> id = decodeStringElement(descriptor, 0)
            1 -> category = decodeStringElement(descriptor, 1)
            2 -> decodeStringElement(descriptor, 2)
            3 -> platform = decodeStringElement(descriptor, 3)
            CompositeDecoder.DECODE_DONE -> break
            else -> error("Unexpected index: $index")
          }
        }
        requireNotNull(id)
        requireNotNull(category)
        requireNotNull(platform)
        fromString(id)
      }
    }

    override fun serialize(encoder: Encoder, value: KotlinTarget) {
      encoder.encodeStructure(descriptor) {
        encodeStringElement(descriptor, 0, value.id)
        encodeStringElement(descriptor, 1, value.category)
        encodeStringElement(descriptor, 2, value.family)
        encodeStringElement(descriptor, 3, value.platform)
      }
    }
  }

  override fun toString(): String = "$category:$platform"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is KotlinTarget) return false

    if (id != other.id) return false
    if (platform != other.platform) return false
    if (family != other.family) return false
    if (category != other.category) return false

    return true
  }

  override fun hashCode(): Int {
    var result = category.hashCode()
    result = 31 * result + family.hashCode()
    result = 31 * result + platform.hashCode()
    result = 31 * result + id.hashCode()
    return result
  }

  companion object {
    fun values(): Set<KotlinTarget> = JS.values() + JVM.values() + Native.values() + Common
    fun fromString(id: String): KotlinTarget {
      return values().find { it.id == id } ?: Unknown(id)
    }
  }

  // ===================================================================================================================

  class Unknown(platform: String) : KotlinTarget(
    category = category,
    platform = platform,
    displayCategory = "Unknown",
  ) {
    companion object {
      const val category = "unknown"
    }
  }

  object Common : KotlinTarget(category = "metadata", platform = "common", displayCategory = "Metadata")

  sealed class JS(platform: String) : KotlinTarget(
    category = category,
    platform = platform,
    displayCategory = "JS",
    id = "${category}_$platform"
  ) {
    companion object {
      const val category = "js"
      fun values(): Set<JS> = setOf(Legacy, IR)
    }

    object Legacy : JS("legacy")
    object IR : JS("ir")
  }

  sealed class JVM(platform: String) : KotlinTarget(
    category = category,
    platform = platform,
    displayCategory = "JVM",
    id = "${category}_$platform"
  ) {
    companion object {
      const val category = "jvm"
      fun values(): Set<JVM> = setOf(Java, Android)
    }

    object Java : JVM("java")
    object Android : JVM("android")
  }

  sealed class Native(
    family: String,
    platform: String,
    id: String = platform
  ) : KotlinTarget(
    category = category,
    platform = platform,
    family = family,
    displayCategory = "Native",
    id = id
  ) {
    companion object {
      const val category = "native"
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

    sealed class AndroidNative(
      val architecture: String,
      id: String
    ) : Native(FAMILY, "$FAMILY$architecture", "${architectureId}_$id") {
      companion object {
        const val FAMILY = "androidNative"
        private const val architectureId = "android"
        fun values(): Set<AndroidNative> =
          setOf(AndroidNativeArm32, AndroidNativeArm64, AndroidNativeX86, AndroidNativeX64)
      }

      object AndroidNativeArm32 : AndroidNative("Arm32", "arm32")
      object AndroidNativeArm64 : AndroidNative("Arm64", "arm64")
      object AndroidNativeX86 : AndroidNative("X86", "x86")
      object AndroidNativeX64 : AndroidNative("X64", "x64")
    }

    sealed class IOS(
      val architecture: String,
      id: String
    ) : Native(FAMILY, "$FAMILY$architecture", "${FAMILY}_$id") {
      companion object {
        const val FAMILY = "ios"
        fun values(): Set<IOS> = setOf(IOSArm32, IOSArm64, IOSX64, IOSSimulatorArm64)
      }

      object IOSArm32 : IOS("Arm32", "arm32")
      object IOSArm64 : IOS("Arm64", "arm64")
      object IOSX64 : IOS("X64", "x64")
      object IOSSimulatorArm64 : IOS("SimulatorArm64", "simulator_arm64")
    }

    sealed class WatchOS(
      val architecture: String,
      id: String
    ) : Native(FAMILY, "$FAMILY$architecture", "${FAMILY}_$id") {
      companion object {
        const val FAMILY = "watchos"
        fun values(): Set<WatchOS> = setOf(WatchOSX86, WatchOSX64, WatchOSArm64, WatchOSArm32, WatchOSSimulatorArm64)
      }

      object WatchOSX86 : WatchOS("X86", "x86")
      object WatchOSX64 : WatchOS("X64", "x64")
      object WatchOSArm64 : WatchOS("Arm64", "arm64")
      object WatchOSArm32 : WatchOS("Arm32", "arm32")
      object WatchOSSimulatorArm64 : WatchOS("SimulatorArm64", "simulator_arm64")
    }

    sealed class TvOS(
      val architecture: String,
      id: String
    ) : Native(FAMILY, "$FAMILY$architecture", "${FAMILY}_$id") {
      companion object {
        const val FAMILY = "tvos"
        fun values(): Set<TvOS> = setOf(TvOSArm64, TvOSX64, TvOSSimulatorArm64)
      }

      object TvOSArm64 : TvOS("Arm64", "arm64")
      object TvOSX64 : TvOS("X64", "x64")
      object TvOSSimulatorArm64 : TvOS("SimulatorArm64", "simulator_arm64")
    }

    sealed class MacOS(
      val architecture: String,
      id: String
    ) : Native(FAMILY, "$FAMILY$architecture", "${FAMILY}_$id") {
      companion object {
        const val FAMILY = "macos"
        fun values(): Set<MacOS> = setOf(MacOSArm64, MacOSX64)
      }

      object MacOSArm64 : MacOS("Arm64", "arm64")
      object MacOSX64 : MacOS("X64", "x64")
    }

    sealed class Mingw(
      val architecture: String,
      id: String
    ) : Native(FAMILY, "$FAMILY$architecture", "${FAMILY}_$id") {
      companion object {
        const val FAMILY = "mingw"
        fun values(): Set<Mingw> = setOf(MingwX64, MingwX86)
      }

      object MingwX64 : Mingw("X64", "x64")

      object MingwX86 : Mingw("X86", "x86")
    }

    sealed class Linux(
      val architecture: String,
      id: String
    ) : Native(FAMILY, "$FAMILY$architecture", "${FAMILY}_$id") {
      companion object {
        const val FAMILY = "linux"
        fun values(): Set<Linux> = setOf(LinuxArm32Hfp, LinuxMips32, LinuxMipsel32, LinuxX64, LinuxArm64)
      }

      object LinuxArm32Hfp : Linux("Arm32Hfp", "arm32_hfp")
      object LinuxMips32 : Linux("Mips32", "mips32")
      object LinuxMipsel32 : Linux("Mipsel32", "mipsel32")
      object LinuxX64 : Linux("X64", "x64")
      object LinuxArm64 : Linux("Arm64", "arm64")
    }
  }
}
