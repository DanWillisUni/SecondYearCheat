# CMAKE generated file: DO NOT EDIT!
# Generated by "MinGW Makefiles" Generator, CMake Version 3.14

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

SHELL = cmd.exe

# The CMake executable.
CMAKE_COMMAND = C:\Apps\CLion-2019.2.win\bin\cmake\win\bin\cmake.exe

# The command to remove a file.
RM = C:\Apps\CLion-2019.2.win\bin\cmake\win\bin\cmake.exe -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = U:\NTProfile\Desktop\cw1

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = U:\NTProfile\Desktop\cw1\cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles/cw1.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/cw1.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/cw1.dir/flags.make

CMakeFiles/cw1.dir/main.c.obj: CMakeFiles/cw1.dir/flags.make
CMakeFiles/cw1.dir/main.c.obj: ../main.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=U:\NTProfile\Desktop\cw1\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building C object CMakeFiles/cw1.dir/main.c.obj"
	C:\Apps\mingw-w64\i686-8.1.0-posix-dwarf\mingw32\bin\gcc.exe $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles\cw1.dir\main.c.obj   -c U:\NTProfile\Desktop\cw1\main.c

CMakeFiles/cw1.dir/main.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/cw1.dir/main.c.i"
	C:\Apps\mingw-w64\i686-8.1.0-posix-dwarf\mingw32\bin\gcc.exe $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E U:\NTProfile\Desktop\cw1\main.c > CMakeFiles\cw1.dir\main.c.i

CMakeFiles/cw1.dir/main.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/cw1.dir/main.c.s"
	C:\Apps\mingw-w64\i686-8.1.0-posix-dwarf\mingw32\bin\gcc.exe $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S U:\NTProfile\Desktop\cw1\main.c -o CMakeFiles\cw1.dir\main.c.s

# Object files for target cw1
cw1_OBJECTS = \
"CMakeFiles/cw1.dir/main.c.obj"

# External object files for target cw1
cw1_EXTERNAL_OBJECTS =

cw1.exe: CMakeFiles/cw1.dir/main.c.obj
cw1.exe: CMakeFiles/cw1.dir/build.make
cw1.exe: CMakeFiles/cw1.dir/linklibs.rsp
cw1.exe: CMakeFiles/cw1.dir/objects1.rsp
cw1.exe: CMakeFiles/cw1.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=U:\NTProfile\Desktop\cw1\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking C executable cw1.exe"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles\cw1.dir\link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/cw1.dir/build: cw1.exe

.PHONY : CMakeFiles/cw1.dir/build

CMakeFiles/cw1.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles\cw1.dir\cmake_clean.cmake
.PHONY : CMakeFiles/cw1.dir/clean

CMakeFiles/cw1.dir/depend:
	$(CMAKE_COMMAND) -E cmake_depends "MinGW Makefiles" U:\NTProfile\Desktop\cw1 U:\NTProfile\Desktop\cw1 U:\NTProfile\Desktop\cw1\cmake-build-debug U:\NTProfile\Desktop\cw1\cmake-build-debug U:\NTProfile\Desktop\cw1\cmake-build-debug\CMakeFiles\cw1.dir\DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/cw1.dir/depend
