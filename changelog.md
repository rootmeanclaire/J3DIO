Beta
====
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
