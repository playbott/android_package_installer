import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'android_package_installer_method_channel.dart';

abstract class AndroidPackageInstallerPlatform extends PlatformInterface {
  /// Constructs a AndroidPackageInstallerPlatform.
  AndroidPackageInstallerPlatform() : super(token: _token);

  static final Object _token = Object();

  static AndroidPackageInstallerPlatform _instance = MethodChannelAndroidPackageInstaller();

  /// The default instance of [AndroidPackageInstallerPlatform] to use.
  ///
  /// Defaults to [MethodChannelAndroidPackageInstaller].
  static AndroidPackageInstallerPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [AndroidPackageInstallerPlatform] when
  /// they register themselves.
  static set instance(AndroidPackageInstallerPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
