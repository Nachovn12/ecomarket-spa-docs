# Guia de Pruebas: Flujo E2E (End-to-End) EcoMarket SPA

El presente documento detalla el flujo de ejecucion tecnica para demostrar la integracion, seguridad y orquestacion del ecosistema de microservicios de EcoMarket. A traves de este flujo, se valida la creacion de usuarios, autenticacion JWT, creacion de carritos, validacion de inventario distribuida, finalizacion de compras y gestion logistica.

---

## Paso 1: Registro de un nuevo Cliente
Se registra un nuevo usuario en la plataforma.

* **Endpoint (API Gateway):** `POST http://localhost:8081/api/usuarios/registro`
* **Microservicio destino:** `ms-usuarios-identidad` (Puerto 8083)
* **Body (JSON):**
  ```json
  {
      "nombre": "Profe Wacoldo",
      "correo": "wacoldo@duoc.cl",
      "password": "PasswordSegura123",
      "telefono": "+56912345678"
  }
  ```
* **Respuesta esperada (201 Created):**
  ```json
  {
      "id": 1,
      "nombre": "Profe Wacoldo",
      "correo": "wacoldo@duoc.cl",
      "rol": "CLIENTE",
      "activo": true,
      "fechaRegistro": "2026-07-15T02:28:17"
  }
  ```
* **Regla de Negocio Aplicada:**
  La peticion ingresa por el API Gateway y es enrutada al Microservicio de Usuarios. Se aplica validacion estricta de datos (Bean Validation) asegurando el formato del correo y atributos obligatorios. La contrasena se almacena cifrada (BCrypt) en la base de datos `bd_usuarios` y el sistema asigna el rol `CLIENTE` automaticamente.

---

## Paso 2: Autenticacion (Login y Emision de JWT)
El usuario inicia sesion para obtener sus credenciales de acceso.

* **Endpoint (API Gateway):** `POST http://localhost:8081/api/auth/login`
* **Microservicio destino:** `ms-usuarios-identidad` (Puerto 8083)
* **Body (JSON):**
  ```json
  {
      "correo": "wacoldo@duoc.cl",
      "password": "PasswordSegura123"
  }
  ```
* **Respuesta esperada (200 OK):**
  ```json
  {
      "mensaje": "Inicio de sesion exitoso",
      "idUsuario": 1,
      "nombre": "Profe Wacoldo",
      "correo": "wacoldo@duoc.cl",
      "rol": "CLIENTE",
      "tokenSesion": "eyJhbGciOiJIUzM4NCJ9...",
      "funcionalidadesDisponibles": [
          "gestionar perfil",
          "gestionar pedidos",
          "consultar historial de compras",
          "gestionar carrito",
          "dejar resenas",
          "solicitar soporte"
      ]
  }
  ```
* **Arquitectura de Seguridad:**
  El microservicio valida las credenciales contra la base de datos. En caso exitoso, el componente JwtProvider genera un token JWT firmado (HS256). El API Gateway actua como proxy pasivo en terminos de seguridad; la validacion y autorizacion del token es delegada a los filtros de seguridad internos de cada microservicio en el ecosistema.

*(Nota: El campo `tokenSesion` de la respuesta se debe copiar y utilizar para autorizar las peticiones posteriores).*

### Como configurar el Token en Postman o Swagger
**Si usas Postman:**
1. Ve a la pestaña **Authorization** (justo debajo de donde pegas la URL).
2. En el menú desplegable "Type" o "Tipo", selecciona **Bearer Token**.
3. En la caja que dice "Token", pega el texto largo que obtuviste en el Login.
4. En la pestaña **Body**, recuerda seleccionar `raw` y luego `JSON` para pegar los datos.

**Si usas Swagger:**
1. Haz clic en el candado o botón verde **"Authorize"** en la parte superior derecha.
2. Pega el Token en la caja de texto y haz clic en Authorize. ¡Listo!
---

## Paso 3: Creacion del Carrito de Compras
El cliente autenticado procede a crear un nuevo carrito.

* **Endpoint (API Gateway):** `POST http://localhost:8081/api/pedidos/carritos`
* **Headers:** `Authorization: Bearer <TOKEN_JWT>`
* **Microservicio destino:** `ms-pedidos-ventas` (Puerto 8086)
* **Body (JSON):**
  ```json
  {
      "idCliente": 1,
      "comentarios": "Primera compra de prueba"
  }
  ```
* **Respuesta esperada (201 Created):**
  ```json
  {
      "idCarrito": 1,
      "idCliente": 1,
      "estado": "ACTIVO",
      "items": [],
      "subtotal": 0.0,
      "total": 0.0
  }
  ```
* **Control de Acceso (IDOR):**
  El ecosistema cuenta con prevencion de vulnerabilidad IDOR (Insecure Direct Object Reference). El sistema compara el `idCliente` de la carga util contra el identificador seguro desencriptado desde el JWT. Si existe discrepancia, se deniega el acceso (403 Forbidden) para proteger la integridad de los datos entre clientes.

---

## Paso 4: Agregar Productos al Carrito
Se anaden items al carrito recien creado.

* **Endpoint (API Gateway):** `POST http://localhost:8081/api/pedidos/carritos/1/items`
* **Headers:** `Authorization: Bearer <TOKEN_JWT>`
* **Microservicio destino:** `ms-pedidos-ventas` (Puerto 8086)
* **Body (JSON):**
  ```json
  {
      "idProducto": 1,
      "nombreProducto": "Bolsa Reutilizable Organica",
      "cantidad": 2,
      "precioUnitario": 4990.0
  }
  ```
* **Respuesta esperada (200 OK):**
  ```json
  {
      "idCarrito": 1,
      "idCliente": 1,
      "estado": "ACTIVO",
      "items": [
          {
              "idItem": 1,
              "idProducto": 1,
              "nombreProducto": "Bolsa Reutilizable Organica",
              "cantidad": 2,
              "precioUnitario": 4990.0,
              "subtotal": 9980.0
          }
      ],
      "subtotal": 9980.0,
      "total": 9980.0
  }
  ```
* **Interoperabilidad y Comunicacion Sincrona:**
  Al recibir la solicitud, el MS Pedidos recibe los datos del producto. El DTO requiere `idProducto`, `nombreProducto`, `cantidad` y `precioUnitario`, los cuales en un flujo real serian obtenidos previamente del `ms-catalogo`. El sistema valida stock contra `ms-inventario-abastecimiento` mediante llamadas internas REST (patron ClientService/RestTemplate).

---

## Paso 5: Crear Pedido desde Carrito (Finalizacion de la Compra)
El cliente confirma y paga su pedido convirtiendo el carrito en un pedido formal.

* **Endpoint (API Gateway):** `POST http://localhost:8081/api/pedidos/desde-carrito/1`
* **Headers:** `Authorization: Bearer <TOKEN_JWT>`
* **Microservicio destino:** `ms-pedidos-ventas` (Puerto 8086)
* **Body (JSON):**
  ```json
  {
      "idCliente": 1,
      "metodoPago": "TARJETA",
      "direccionEntrega": "Av. Apoquindo 4500, Las Condes",
      "observaciones": "Entrega urgente"
  }
  ```
* **Respuesta esperada (201 Created):**
  ```json
  {
      "idPedido": 1,
      "idCliente": 1,
      "nombreCliente": "Profe Wacoldo",
      "correoCliente": "wacoldo@duoc.cl",
      "estado": "PENDIENTE",
      "metodoPago": "TARJETA",
      "direccionEntrega": "Av. Apoquindo 4500, Las Condes",
      "subtotal": 9980.0,
      "iva": 1896.2,
      "total": 9980.0
  }
  ```
* **Orquestacion de Reglas de Negocio:**
  El proceso convierte el carrito de estado `ACTIVO` a `CONVERTIDO`. El servicio realiza comunicacion inter-microservicio con `ms-usuarios-identidad` para recuperar los datos del cliente (nombre, correo) y consolidar el pedido formal en base de datos. Se aplica validacion IDOR para asegurar que el `idCliente` del JWT coincida con el solicitante. Los metodos de pago validos son: `TARJETA`, `TRANSFERENCIA`, `EFECTIVO`.

---

## Paso 6: Gestion Logistica del Envio (Rol ADMINISTRADOR)
Un administrador del sistema gestiona el despacho del pedido creado.

* **Login como Administrador:** `POST http://localhost:8081/api/auth/login`
  ```json
  {
      "correo": "admin@ecomarket.cl",
      "password": "Password1"
  }
  ```

* **Endpoint (directo al MS):** `POST http://localhost:8087/api/envios`
* **Headers:** `Authorization: Bearer <TOKEN_ADMIN>`
* **Microservicio destino:** `ms-logistica-envios` (Puerto 8087)
* **Body (JSON):**
  ```json
  {
      "idPedido": 1,
      "origen": "Bodega Central EcoMarket, Santiago",
      "destino": "Av. Apoquindo 4500, Las Condes",
      "estado": "PREPARADO",
      "ubicacionActual": "Bodega Central",
      "observacion": "Envio generado para pedido 1"
  }
  ```
* **Respuesta esperada (201 Created):**
  ```json
  {
      "id": 1,
      "idPedido": 1,
      "origen": "Bodega Central EcoMarket, Santiago",
      "destino": "Av. Apoquindo 4500, Las Condes",
      "estado": "PREPARADO",
      "fechaEstimadaEntrega": "2026-07-18T02:39:43",
      "fechaCreacion": "2026-07-15T02:39:43"
  }
  ```
* **Control de Acceso por Rol (RBAC):**
  El MS de Logistica aplica control de acceso basado en roles. Solo los usuarios con rol `ADMINISTRADOR` o `EMPLEADO` pueden crear y gestionar envios. Si un `CLIENTE` intenta acceder, recibe un 403 Forbidden. Los estados validos del envio son: `PREPARADO`, `EN_CAMINO`, `ENTREGADO`, `CON_INCIDENCIA`.

---

## Verificacion de Seguimiento del Envio
Se verifica la trazabilidad del envio creado.

* **Endpoint:** `GET http://localhost:8087/api/envios/1/seguimiento`
* **Headers:** `Authorization: Bearer <TOKEN_ADMIN>`
* **Respuesta esperada (200 OK):**
  ```json
  {
      "id": 1,
      "estado": "PREPARADO",
      "observacion": "Envio creado y en preparacion",
      "actualizadoPor": "Sistema",
      "fechaRegistro": "2026-07-15T02:39:43",
      "envio": {
          "id": 1,
          "idPedido": 1,
          "estado": "PREPARADO",
          "origen": "Bodega Central EcoMarket, Santiago",
          "destino": "Av. Apoquindo 4500, Las Condes"
      }
  }
  ```
* **Resultado:**
  El sistema genera automaticamente un registro de seguimiento al momento de crear el envio, confirmando la trazabilidad completa del flujo End-to-End del ecosistema.





