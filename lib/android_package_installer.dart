
import 'android_package_installer_platform_interface.dart';

class AndroidPackageInstaller {
  Future<String?> getPlatformVersion() {
    return AndroidPackageInstallerPlatform.instance.getPlatformVersion();
  }
}
