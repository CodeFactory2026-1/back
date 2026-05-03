package com.tareasdomesticas.hogar_service.tareas.domain.model;

public enum EstadoTarea {

    PENDIENTE {
        @Override
        public boolean puedeTransicionarA(EstadoTarea nuevo) {
            return nuevo == ASIGNADO;
        }
    },
    ASIGNADO {
        @Override
        public boolean puedeTransicionarA(EstadoTarea nuevo) {
            return nuevo == EN_PROCESO || nuevo == PENDIENTE;
        }
    },
    EN_PROCESO {
        @Override
        public boolean puedeTransicionarA(EstadoTarea nuevo) {
            return nuevo == FINALIZADO || nuevo == PENDIENTE;
        }
    },
    FINALIZADO {
        @Override
        public boolean puedeTransicionarA(EstadoTarea nuevo) {
            return false;
        }
    };
    public abstract boolean puedeTransicionarA(EstadoTarea nuevo);
}
