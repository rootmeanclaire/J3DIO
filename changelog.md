Beta
====

5.0
---
 * Added `.dae` file support
 * Added javadoc

4.3
---
 * Added JOGL render support

4.2
-----
 * OpenGL render support for `.ply` models
 * Internal improvements in `.ply` file processing

4.1.3
-----
 * Fixed `.mtl` render bug

4.1.2
-----
 * Fixed `.obj` render bug

4.1.1
-----
 * Fixed `.mtl` loader
 * Created `Definable`
 * Changed how colors are stored in `MtlModel`s

4.1
---
 * Fixed `.raw` loading glitch
 * `ObjModel`
	 * Fixed render glitch
	 * Fixed texture coordinate processing
 * `MtlMaterial`
	 * Can store multiple illumination models
	 * Can store index of refraction
	 * Altered comment processing
 * Suppressed warnings on `PlyModel`
 * Deprecated `GLRenderable.render()`
	 * Replaced with `GLRenderable.glrender()`
 * Added `toString()` for `Point3f`
 * Commented

4.0
---
 * Added `.stl` file support
 * Updated top-level package name

3.0
---
 * Added `.ply` file support
 * Added `exportGroup()` method to `MtlMaterial`
	 * It is now possible to export several MtlMaterials to the same file

2.1
---
 * Added OpenGL rendering support for `ObjModel`
 * Added OpenGL rendering support for `RawModel`
 * Renamed `Face` to `ObjFace`

2.0
---
 * Added `.raw` file support
 * Moved `Point3f` from `jml.obj` to `jml`
 * `jml.obj.Face` now extends `jml.raw.RawFace`
