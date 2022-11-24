import 'package:flutter/material.dart';
import 'dart:async';
import 'package:permission_handler/permission_handler.dart';

import 'package:flutter/services.dart';
import 'package:android_package_installer/android_package_installer.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _installationStatus = '';

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              const SizedBox(height: 30),
              _button('Install apk file', () async {
                try {
                  int? code = await AndroidPackageInstaller.installApk(apkFilePath: '/sdcard/Download/sb.apk');
                  if (code != null) {
                    setState(() {
                      _installationStatus = PackageInstallerStatus.getStatusByCode(code).name;
                    });
                  }
                } on PlatformException {
                  print('Failed to share apk file.');
                }
              }),
              const SizedBox(height: 30),
              Text('_installationStatus: $_installationStatus'),
              const Spacer(),
              SizedBox(
                child: Column(children: [
                  const Text('Permissions:'),
                  _button('External Storage', () => _requestPermission(Permission.storage)),
                  _button('Request Install Packages', () => _requestPermission(Permission.requestInstallPackages)),
                  _button('Manage External Storage\n(for Android 11+ target)',
                      () => _requestPermission(Permission.manageExternalStorage)),
                ]),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

ElevatedButton _button(String text, VoidCallback? onPressed) {
  return ElevatedButton(
    onPressed: onPressed,
    child: Text(text),
  );
}

void _requestPermission(Permission permission) async {
  var status = await permission.status;
  if (status.isDenied) {
    await permission.request();
  }
}
