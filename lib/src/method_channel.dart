import 'package:flutter/services.dart';

import 'installer_platform.dart';

class MethodChannelAndroidPackageInstaller
    extends AndroidPackageInstallerPlatform {
  final methodChannel = const MethodChannel('android_package_installer');

  @override
  Future<int?> installApk(String path) async {
    final result = await methodChannel.invokeMethod<int>('installApk', path);
    return result;
  }
}
