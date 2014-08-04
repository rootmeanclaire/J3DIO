J3DIO
======
Java 3D Input Output, or J3DIO(say "jedio"), is a file IO system for 3D models. 

File type support
-----------------
| File type | Import  | Export  | Render  |
|-----------|---------|---------|---------|
|   .obj    | Yes     | Yes     | Yes     |
|   .mtl    | Yes     | Yes     | Partial |
|   .raw    | Yes     | Yes     | Yes     |
|   .ply    | Partial | Partial | No      |
|   .dae    | No      | No      | No      |
|   .fbx    | No      | No      | No      |

###`.obj` and `.mtl`###
**Import:** Textures not supported  
**Render:** Materials not supported

###`.raw`###
**Import:** Full support  
**Render:** Full support

###`.ply`###
**Import:** Binary not supported  
**Render:** Not supported

###`.stl`###
**Import:** In-dev  
**Render:** N/A

###`.dae`###
**Import:** N/A  
**Render:** N/A

###`.fbx`###
**Import:** N/A  
**Render:** N/A

Download
--------
Click [here][1] to download the latest version of J3DIO for Java 7

 [1]: https://github.com/FracturedRetina/J3DIO/releases/download/v3.0-beta/jml_v3.0-beta_jre7.jar
