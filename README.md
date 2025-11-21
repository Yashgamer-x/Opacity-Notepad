Opacity-Notepad
Opacity-Notepad is a lightweight, cross-platform JavaFX notepad focused on simplicity, clarity, and a distraction‑free writing experience. It packages as a runnable JAR, platform installers (EXE/MSI on Windows, DEB/RPM on Linux), an app-image, and a distributable ZIP that can include a bundled runtime for systems without Java.
Features
- Minimal, fast notepad UI built with JavaFX
- Cross-platform packaging: Windows installers (EXE/MSI), Linux packages (DEB/RPM), app-image and ZIP distribution options
- Optional bundled runtime so users don’t need a system JRE
- Installer creates shortcuts and can prompt for install directory (Windows EXE with --win-dir-chooser / MSI UI)
- Easy portable ZIP with platform launchers for quick distribution
Getting started (for developers)
Prerequisites
- JDK 17+ with jpackage (for platform installers)
- Maven
- For Linux packages build on Linux (WSL/Docker/CI) — Windows jpackage cannot produce .deb/.rpm
Build and package (common)
- Build the project and create the runnable jar:
mvn clean package


- Prepare input folder for jpackage:
mkdir -p target/app/libs
cp target/notepad-1.0-SNAPSHOT.jar target/app/libs/


Windows (EXE / MSI)
- Create an app-image:
jpackage.exe --type app-image --name "Opacity-Notepad" --input target\app\libs --main-jar notepad-1.0-SNAPSHOT.jar --main-class org.yashgamerx.notepad.NotepadApplication --runtime-image target\image --dest target\installer --icon src\main\resources\icon.ico


- Create an EXE installer (directory chooser + shortcuts):
jpackage.exe --type exe --name "Opacity-Notepad" --app-version 1.0 --input target\app\libs --main-jar notepad-1.0-SNAPSHOT.jar --main-class org.yashgamerx.notepad.NotepadApplication --runtime-image target\image --dest target\installer --icon src\main\resources\icon.ico --win-shortcut --win-menu --win-dir-chooser


- Create an MSI installer:
jpackage.exe --type msi --name "Opacity-Notepad" --app-version 1.0 --input target\app\libs --main-jar notepad-1.0-SNAPSHOT.jar --main-class org.yashgamerx.notepad.NotepadApplication --runtime-image target\image --dest target\installer --icon src\main\resources\icon.ico --win-shortcut --win-menu


Linux (DEB / RPM)
- Build .deb/.rpm on Linux/WSL/Docker/CI (example in WSL):
# in WSL or Linux shell after copying project
mvn clean package
mvn javafx:jlink
mkdir -p target/app/libs
cp target/notepad-1.0-SNAPSHOT.jar target/app/libs/
jpackage --type deb --name "Opacity-Notepad" --app-version 1.0 --input target/app/libs --main-jar notepad-1.0-SNAPSHOT.jar --main-class org.yashgamerx.notepad.NotepadApplication --runtime-image target/image --dest target/installer --icon src/main/resources/icon.png --vendor "Yashgamerx"


Note: Use the Linux jpackage binary (not the Windows one) to produce .deb/.rpm.
ZIP distribution (portable)
- Create a distributable folder, add runnable JAR, JavaFX platform jars or bundled JRE, and platform launchers. Example:
mkdir -p dist/Opacity-Notepad/lib
cp target/notepad-1.0-SNAPSHOT.jar dist/Opacity-Notepad/lib/
# optionally copy JavaFX platform jars or jlink runtime into dist/Opacity-Notepad/jre
# add start-notepad.bat and run-notepad.sh launchers
zip -r dist/Opacity-Notepad.zip dist/Opacity-Notepad


Installing the application (User instructions)
Windows
- Download the installer from the Executable folder (look for the EXE or MSI file).
- Run the EXE or MSI and follow the installer UI. The EXE installer can prompt you to select the installation directory and will create Start Menu/desktop shortcuts if you choose those options. If you receive a ZIP instead, extract it and run the included start-notepad.bat or the Notepad executable inside the app folder.
Linux
- Download the DEB (Debian/Ubuntu) or RPM (Fedora/CentOS) package from the Executable folder.
- Install with:
- Debian/Ubuntu: sudo apt install ./path/to/opacity-notepad-1.0-1.deb
- RPM: sudo rpm -i path/to/opacity-notepad-1.0-1.rpm
- Alternatively, extract the ZIP and run the run-notepad.sh script (ensure it is executable).
Portable ZIP
- Extract the ZIP and run the platform launcher:
- Windows: start-notepad.bat (or Notepad.exe in the extracted app folder)
- Linux: ./run-notepad.sh
Important: If you did not bundle a runtime, ensure a compatible Java 17+ JRE is installed on the machine. If you want the app to run without installing Java, use the installers that include the bundled runtime or provide the ZIP with a bundled jlink runtime image.
Troubleshooting
- Installer UI does not appear: verify the produced artifact is an MSI/EXE in target/installer, then run msiexec /i "<path>.msi" with verbose logging or run the EXE interactively.
- JavaFX not present in runtime: ensure JavaFX platform jars (with correct classifier: win/linux/mac) are on the module-path or include a jlink runtime that contains javafx modules.
- Cannot produce .deb/.rpm on Windows: build those packages inside WSL, Docker, or a Linux CI runner.
- Git push rejected: run git fetch origin && git rebase origin/master (save local changes first) then git push origin master.
Contributing
- Fork, branch, make changes, and open a pull request.
- Keep builds reproducible and avoid shading or repackaging JavaFX; prefer official platform classifiers.
- If you add platform-specific packaging steps or CI workflows, include them under .github/workflows or docs/packaging.md.
License
Include your preferred license file (LICENSE) in the repository root.
Author
Yash Desai (Yashgamerx) — software engineer focused on robust cross-platform packaging and secure, vendor-trusted workflows
