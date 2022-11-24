
import '../android_package_installer.dart';

class AndroidPackageInstaller {
  static Future<int?> installApk({required String apkFilePath}) {
    Future<int?> code = AndroidPackageInstallerPlatform.instance.installApk(apkFilePath);
    return code;
  }
}
