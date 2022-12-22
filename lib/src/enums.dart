///Package installer result status
enum PackageInstallerStatus {
  //TODO: maybe will use with event channel
  // pendingUserAction(-1),
  success(0),
  failure(1),
  failureBlocked(2),
  failureAborted(3),
  failureInvalid(4),
  failureConflict(5),
  failureStorage(6),
  failureIncompatible(7),
  unknown(-2);

  final int code;

  ///Get enum type by status code
  static PackageInstallerStatus byCode(int code) {
    PackageInstallerStatus status = PackageInstallerStatus.unknown;
    try {
      status = PackageInstallerStatus.values.firstWhere((element) => (code == element.code));
    } catch (_) {
      return status;
    }
    return status;
  }

  const PackageInstallerStatus(this.code);
}
