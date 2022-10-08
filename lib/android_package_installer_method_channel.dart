import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'android_package_installer_platform_interface.dart';

/// An implementation of [AndroidPackageInstallerPlatform] that uses method channels.
class MethodChannelAndroidPackageInstaller extends AndroidPackageInstallerPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('android_package_installer');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
