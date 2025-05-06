package com.tfg.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 🔧 Configuración global de la aplicación.
 * 
 * Esta clase define beans reutilizables que pueden ser inyectados en cualquier parte del sistema.
 * Se declara como `@Configuration` para que Spring la reconozca como una fuente de configuración.
 */
@Configuration
public class AppConfig {

    /**
     *  Bean de `RestTemplate`: Cliente HTTP para consumo de APIs externas.
     * 
     * Se registra `RestTemplate` como un componente inyectable dentro del contexto de Spring.
     * Este objeto se utiliza para hacer peticiones HTTP REST a otros servicios.
     * 
     *  Consideraciones:
     * - Si en el futuro se requiere gestión avanzada de conexiones, usar `RestTemplateBuilder`.
     * - Para aplicaciones reactivas, considerar `WebClient` (más flexible y compatible con programación reactiva).
     * 
     * @return instancia de `RestTemplate` lista para ser usada.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
