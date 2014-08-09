J3DIO
======
Java 3D Input Output, or J3DIO(say "jed-ee-oh"), is a file IO system for 3D models. This is a personal project, but I think that this libray might be useful to someone.

Installation
------------
Click [here][1] to download the latest version of J3DIO for Java 7

Contribute
----------
Feel free to [fork this repository][2]. If you want information about a certain file type's specifications, I've been compiling a [list][3] on the wiki, which you are more than welcome to add to with your own findings.

If you'd like to bring something to the attention of the developers, whether an addition or bug, [open an issue][4];

Help
----
 * Wiki
 * Javadoc
 * [Website][7]

What's new
----------
 * Fixed `.mtl` loader
 * Creted `Definable`
 * Changed how colors are stored in `MtlModel`s

File type support
-----------------
| File type | Import  | Export  | Render  |
|-----------|---------|---------|---------|
|   .obj    | Yes     | Yes     | Yes     |
|   .mtl    | Yes     | Yes     | No      |
|   .raw    | Yes     | Yes     | Yes     |
|   .ply    | Partial | Partial | Partial |
|   .stl    | Partial | Partial | Partial |
|   .dae    | No      | No      | No      |
|   .fbx    | No      | No      | No      |

####`.obj` and `.mtl`####
**Import/Export:** Textures not supported  
**Render:** Materials not supported

####`.raw`####
**Import/Export:** Full support  
**Render:** Full support

####`.ply`####
**Import/Export:** Binary, Colors, and Textures not supported  
**Render:** Not supported

####`.stl`####
**Import/Export:** Colors not supported  
**Render:** Colors not supported

Contributors
------------
 * Evan Shimoniak

 [1]: https://github.com/FracturedRetina/J3DIO/releases/download/v4.2-beta/j3dio_v4.2-beta.jar
 [2]: https://github.com/FracturedRetina/J3DIO/fork
 [3]: https://github.com/FracturedRetina/J3DIO/wiki/File-Type-Resources
 [4]: https://github.com/FracturedRetina/J3DIO/issues
 [5]: https://github.com/FracturedRetina/J3DIO/wiki
 [6]: http://fracturedretina.github.io/j3dio/doc/doc_v4.2-beta/
 [7]: http://fracturedretina.github.io/j3dio/
