# Opacity-Notepad

Opacity-Notepad is a lightweight, cross-platform JavaFX notepad focused on simplicity, clarity, and a distraction‑free writing experience. It can be distributed as a runnable JAR, platform installers (EXE/MSI on Windows, DEB/RPM on Linux), an app-image, or a downloadable ZIP that may include a bundled runtime so users don’t need to install Java.

---

## Features
- **Minimal UI** built with JavaFX for fast, distraction‑free writing  
- **Cross-platform packaging**: Windows installers (EXE/MSI), Linux packages (DEB/RPM), app-image, ZIP distribution  
- **Optional bundled runtime** so target machines can run without a system JRE  
- **Installer conveniences**: Windows EXE supports directory chooser and Start Menu/desktop shortcuts; MSI supports standard installer UI  
- **Portable ZIP** with platform launchers for quick distribution and testing

---

## Getting Started for Developers
Prerequisites:
- **JDK 17+** with jpackage for platform installers  
- **Maven**  
- Build Linux packages on Linux (WSL/Docker/CI); Windows jpackage cannot produce DEB/RPM

Basic build and package steps:
```bash
mvn clean package

mkdir -p target/app/libs
cp target/notepad-1.0-SNAPSHOT.jar target/app/libs/
```

Windows packaging examples:
- App-image:
```powershell
jpackage.exe --type app-image --name "Opacity-Notepad" --input target\app\libs --main-jar notepad-1.0-SNAPSHOT.jar --main-class org.yashgamerx.notepad.NotepadApplication --runtime-image target\image --dest target\installer --icon src\main\resources\icon.ico
```
- EXE installer (directory chooser + shortcuts):
```powershell
jpackage.exe --type exe --name "Opacity-Notepad" --app-version 1.0 --input target\app\libs --main-jar notepad-1.0-SNAPSHOT.jar --main-class org.yashgamerx.notepad.NotepadApplication --runtime-image target\image --dest target\installer --icon src\main\resources\icon.ico --win-shortcut --win-menu --win-dir-chooser
```
- MSI installer:
```powershell
jpackage.exe --type msi --name "Opacity-Notepad" --app-version 1.0 --input target\app\libs --main-jar notepad-1.0-SNAPSHOT.jar --main-class org.yashgamerx.notepad.NotepadApplication --runtime-image target\image --dest target\installer --icon src\main\resources\icon.ico --win-shortcut --win-menu
```

Linux packaging (run on Linux/WSL/Docker/CI):
```bash
mvn clean package
mvn javafx:jlink
mkdir -p target/app/libs
cp target/notepad-1.0-SNAPSHOT.jar target/app/libs/
jpackage --type deb --name "Opacity-Notepad" --app-version 1.0 --input target/app/libs --main-jar notepad-1.0-SNAPSHOT.jar --main-class org.yashgamerx.notepad.NotepadApplication --runtime-image target/image --dest target/installer --icon src/main/resources/icon.png --vendor "Yashgamerx"
```
Note: Use the Linux jpackage binary (not the Windows one) to produce DEB/RPM.

ZIP distribution (portable):
```bash
mkdir -p dist/Opacity-Notepad/lib
cp target/notepad-1.0-SNAPSHOT.jar dist/Opacity-Notepad/lib/
# optionally copy JavaFX platform jars or jlink runtime into dist/Opacity-Notepad/jre
# add platform launchers start-notepad.bat and run-notepad.sh
zip -r dist/Opacity-Notepad.zip dist/Opacity-Notepad
```

---

## Installing and Running
Windows
- **Download the installer from the Executable folder** (look for the .exe or .msi file).  
- Run the EXE or MSI and follow the installer UI. The EXE installer can prompt for the installation directory and will create Start Menu/desktop shortcuts if selected.  
- If you downloaded a ZIP instead: extract it and run the included start-notepad.bat or the Notepad executable inside the extracted app folder.

Linux
- **Download the DEB or RPM from the Executable folder**.  
- Install with:
  - Debian/Ubuntu: `sudo apt install ./path/to/opacity-notepad-1.0-1.deb`  
  - RPM: `sudo rpm -i path/to/opacity-notepad-1.0-1.rpm`  
- Or extract the ZIP and run `./run-notepad.sh` (ensure executable permission).

Portable ZIP
- Extract the ZIP and run the platform launcher:
  - Windows: `start-notepad.bat` or the Notepad executable in the extracted folder  
  - Linux: `./run-notepad.sh`

Important: If the package does not include a bundled runtime, ensure a compatible **Java 17+ JRE** is installed on the machine. To avoid requiring a system JRE, distribute installers or ZIPs that include a bundled jlink runtime.

---

## Troubleshooting, Contributing, and License
Troubleshooting
- Installer UI not appearing: confirm the artifact in `target/installer` is an MSI/EXE and run `msiexec /i "<path>.msi" /l*v "%TEMP%\opacity-install.log"` to collect a verbose log.  
- JavaFX missing at runtime: ensure JavaFX platform jars use the correct classifier (win/linux/mac) or include a jlink runtime that contains javafx modules.  
- Cannot produce DEB/RPM on Windows: build those packages inside WSL, Docker, or a Linux CI runner.  
- Git push rejected: run `git fetch origin && git rebase origin/master` (save local changes first) then `git push origin master`.

Contributing
- Fork the repo, create a feature branch, submit a pull request.  
- Keep builds reproducible; prefer official JavaFX platform classifiers and avoid shading JavaFX.  
- Add platform packaging changes or CI workflows under `.github/workflows` or `docs/packaging.md`.

License
- Include a LICENSE file in the repository root. Recommended: **Apache License 2.0** (permissive with an explicit patent grant) or **MIT** for maximum simplicity.

---

## Author
**Yash Desai (Yashgamerx)** — software engineer focused on robust cross-platform packaging and vendor‑trusted workflows

---
