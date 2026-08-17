{ pkgs ? import <nixpkgs> {} }:
pkgs.mkShell {
  packages = with pkgs; [
    jdk25
    gradle_9
    maven

    python3Packages.requests
    python3Packages.click

    glfw
    libglvnd
  ];

  LD_LIBRARY_PATH = with pkgs; lib.makeLibraryPath [glfw libglvnd];
}
