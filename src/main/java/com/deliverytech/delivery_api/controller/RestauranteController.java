package com.deliverytech.delivery_api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.entity.RestauranteDTO;
import com.deliverytech.delivery_api.services.RestauranteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Restaurantes", description = "Gerenciamento de restaurantes (CRUD, filtros e paginação).")
@RestController
@RequestMapping("/api/restaurantes")
@CrossOrigin(origins = "*")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    // ===============================================================
    // 🟢 Cadastrar novo restaurante
    // ===============================================================
    @Operation(
        summary = "Cadastrar novo restaurante",
        description = "Cria um novo restaurante ativo no sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Restaurante cadastrado com sucesso",
            content = @Content(schema = @Schema(implementation = Restaurante.class))),
        @ApiResponse(responseCode = "400", description = "Erro de validação ou dados inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<?> cadastrar(
            @Validated
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados do restaurante a ser cadastrado",
                required = true,
                content = @Content(examples = @ExampleObject(
                    value = """
                    {
                      "nome": "Pizza Express",
                      "categoria": "Pizza",
                      "endereco": "Rua das Oliveiras, 123",
                      "telefone": "11999999999",
                      "taxaEntrega": 5.50
                    }
                    """)
                )
            )
            Restaurante restaurante) {

        try {
            Restaurante restauranteSalvo = restauranteService.cadastrar(restaurante);
            return ResponseEntity.status(HttpStatus.CREATED).body(restauranteSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    // ===============================================================
    // 🟡 Listar restaurantes ativos
    // ===============================================================
    @Operation(
        summary = "Listar restaurantes ativos",
        description = "Retorna a lista de restaurantes ativos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<RestauranteDTO> restaurantes = restauranteService.listarAtivos();
            return ResponseEntity.ok(restaurantes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    // ===============================================================
    // 🟠 Buscar restaurante por ID
    // ===============================================================
    @Operation(
        summary = "Buscar restaurante por ID",
        description = "Recupera os dados de um restaurante específico."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Optional<RestauranteDTO> restaurante = restauranteService.findById(id);
            return restaurante
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Restaurante não encontrado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    // ===============================================================
    // 🟣 Atualizar restaurante
    // ===============================================================
    @Operation(
        summary = "Atualizar restaurante",
        description = "Atualiza os dados de um restaurante existente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @Validated @RequestBody Restaurante restaurante) {
        try {
            Restaurante atualizado = restauranteService.atualizar(id, restaurante);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    // ===============================================================
    // 🔴 Excluir restaurante
    // ===============================================================
    @Operation(
        summary = "Excluir restaurante",
        description = "Remove um restaurante permanentemente do banco de dados."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Restaurante excluído com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(
            @Parameter(description = "ID do restaurante a ser removido", example = "1")
            @PathVariable Long id) {
        try {
            restauranteService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    // ===============================================================
    // ⚪ Inativar restaurante
    // ===============================================================
    @Operation(
        summary = "Inativar restaurante",
        description = "Marca o restaurante como inativo (não removendo do banco)."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante inativado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}/inativar")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        try {
            restauranteService.inativar(id);
            return ResponseEntity.ok().body("Restaurante inativado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    // ===============================================================
    // 🔵 Buscar por categoria
    // ===============================================================
    @Operation(
        summary = "Buscar restaurantes por categoria",
        description = "Retorna restaurantes filtrados pela categoria informada."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum restaurante encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<?> buscarPorCategoria(
            @Parameter(description = "Categoria do restaurante", example = "Pizza")
            @PathVariable String categoria) {
        try {
            return ResponseEntity.ok(restauranteService.buscarPorCategoria(categoria));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }
}
