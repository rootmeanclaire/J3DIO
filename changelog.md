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