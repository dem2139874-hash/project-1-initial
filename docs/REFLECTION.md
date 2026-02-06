# Reflection Log

This document captures reflections on the development of 3D geometric classes in Java, focusing on design patterns, principles, and lessons learned.

I looked up the immutability pattern, and while using immutability it ensures that a point used in multiple geometric structures cannot be unintentionally altered, which is especially important
as Point3D objects are shared by Line3D and Cube3D.