cd D:\Android\workspace\FPlay\obj\local\x86_64\objs\SimpleVisualizerJni
D:\Android\sdk\ndk-bundle\toolchains\x86-4.9\prebuilt\windows-x86_64\bin\i686-linux-android-objdump -S -d NeonFunctions.o > x86.txt
move x86.txt D:\Android\workspace\FPlay\jni\x86_64.txt

cd D:\Android\workspace\FPlay\obj\local\armeabi-v7a\objs\SimpleVisualizerJni
D:\Android\sdk\ndk-bundle\toolchains\arm-linux-androideabi-4.9\prebuilt\windows-x86_64\bin\arm-linux-androideabi-objdump -S -d NeonFunctions.o > armeabi.txt
move armeabi.txt D:\Android\workspace\FPlay\jni\armeabi.txt

cd D:\Android\workspace\FPlay\obj\local\x86\objs\SimpleVisualizerJni
D:\Android\sdk\ndk-bundle\toolchains\x86-4.9\prebuilt\windows-x86_64\bin\i686-linux-android-objdump -S -d NeonFunctions.o > x86.txt
move x86.txt D:\Android\workspace\FPlay\jni\x86.txt

pause
