package com.tareasdomesticas.hogar_service.auth.application.port.in;
public record RegistrarUsuarioCommand(String nombre, String correo,
                                      String contrasena, String confirmacionContrasena) {}
