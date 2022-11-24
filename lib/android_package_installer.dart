import 'package:android_package_installer/src/method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

export 'package:android_package_installer/src/method_channel.dart';
export 'package:android_package_installer/src/methods.dart';
export 'package:android_package_installer/src/enums.dart';

abstract class AndroidPackageInstallerPlatform extends PlatformInterface {
  AndroidPackageInstallerPlatform() : super(token: _token);

  static final Object _token = Object();

  static AndroidPackageInstallerPlatform _instance = MethodChannelAndroidPackageInstaller();
  static AndroidPackageInstallerPlatform get instance => _instance;

  static set instance(AndroidPackageInstallerPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<int?> installApk(String path) {
    throw UnimplementedError('installApk() has not been implemented.');
  }
}
