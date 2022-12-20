import 'package:android_package_installer/src/installer_platform.dart';

export 'package:android_package_installer/src/method_channel.dart';
export 'package:android_package_installer/src/enums.dart';

class AndroidPackageInstaller {
  static Future<int?> installApk({required String apkFilePath}) {
    Future<int?> code = AndroidPackageInstallerPlatform.instance.installApk(apkFilePath);
    return code;
  }
}
