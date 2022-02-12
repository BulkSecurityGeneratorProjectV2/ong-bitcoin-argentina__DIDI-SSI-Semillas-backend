package com.atixlabs.semillasmiddleware.app.model.excel;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Form {

    @ExcelRow
    private int rowIndex;

    @ExcelCellName("_index")
    private int index;

    @ExcelCellName("Actividad_Tipo_Comercio")
    private String actividadTipoComercio;


    @ExcelCellName("Grupo Solidario")
    private String grupoSolidario;

    @ExcelCellName("Asesora")
    private String asesora;

    @ExcelCellName("Nombre_Beneficiario")
    private String nombreBeneficiario;

    @ExcelCellName("Apellido_Beneficiario")
    private String apellidoBeneficiario;

    @ExcelCellName("Fecha_Nacimiento_Beneficiario")
    private String fechaNacimientoBeneficiario;

    @ExcelCellName("Edad_Beneficiario")
    private String edadBeneficiario;

    @ExcelCellName("Estado_Civil_Beneficiario")
    private String estadoCivilBeneficiario;

    @ExcelCellName("Estado_Civil_Otro_Beneficiario")
    private String estadoCivilOtroBeneficiario;

    @ExcelCellName("Genero_Beneficiario")
    private String generoBeneficiario;

    @ExcelCellName("Genero_Otro_Beneficiario")
    private String generoOtroBeneficiario;

    @ExcelCellName("Numero_Doc_Beneficiario")
    private Long numeroDniBeneficiario;

    @ExcelCellName("Tienen_Hijos")
    private String tieneHijos;

    @ExcelCellName("Hay_Mas_Miembros_Familia")
    private String tieneMasFamilia;

    @ExcelCellName("Vivienda_Cambios")
    private String huboCambiosVivienda;

    // Datos del conyuge
    @ExcelCellName("Apellido_Conyuge")
    private String apellidoConyuge;

    @ExcelCellName("Nombre_Conyuge")
    private String nombreConyuge;

    @ExcelCellName("Fecha_Nacimiento_Conyuge")
    private String fechaNacimientoConyuge;

    @ExcelCellName("Edad_Conyuge")
    private String edadConyuge;

    @ExcelCellName("Genero_Conyuge")
    private String generoConyuge;

    @ExcelCellName("Genero_Otro_Conyuge")
    private String generoOtroConyuge;

    @ExcelCellName("Tipo_Documento_Conyuge")
    private String tipoDocumentoConyuge;

    @ExcelCellName("Numero_Doc_Conyuge")
    private Long numeroDniConyuge;

    //Datos de Vivienda

    @ExcelCellName("Vivienda_Tipo_Tenencia")
    private String viviendaTipoTenencia;

    @ExcelCellName("Vivienda_Tipo_Tenencia_Otro")
    private String viviendaTipoTenenciaOtro;

    @ExcelCellName("Vivienda_Tipo_Vivienda")
    private String viviendaTipoVivienda;

    @ExcelCellName("Vivienda_Tipo_Vivienda_Otro")
    private String viviendaTipoViviendaOtro;

    @ExcelCellName("Vivienda_Gas_Red de gas")
    private Integer viviendaRedDeGas;

    @ExcelCellName("Vivienda_Gas_Garrafa")
    private Integer viviendaGarrafa;

    @ExcelCellName("Vivienda_Agua_Red de Agua")
    private Integer viviendaRedDeAgua;

    @ExcelCellName("Vivienda_Agua_ Bomba")
    private Integer viviendaBomba;

    @ExcelCellName("Vivienda_Instalacion_Luz")
    private String viviendaInstalacionLuz;

    @ExcelCellName("Vivienda_General")
    private String viviendaCondicionesGenerales;

    @ExcelCellName("Vivienda_General_Otro")
    private String viviendaCondicionesGeneralesOtro;

    @ExcelCellName("Vivienda_Tipo_Barrio")
    private String viviendaTipoBasrrio;

    @ExcelCellName("Vivienda_Tipo_Barrio_Otro")
    private String viviendaTipoBarrioOtro;

    @ExcelCellName("Dom1_Calle_Beneficiario")
    private String viviendaDireccionCalle;

    @ExcelCellName("Dom1_Num_Beneficiario")
    private String viviendaDireccionNumero;

    @ExcelCellName("Dom1_Entre_Calles_Beneficiario")
    private String viviendaDireccionEntreCalles;

    @ExcelCellName("Dom1_Municipio_Beneficiario") //Distrito
    private String viviendaDireccionMunicipio;

    @ExcelCellName("Dom1_Localidad_Beneficiario")
    private String viviendaDomicilioLocalidad;

    @ExcelCellName("Dom1_Barrio_Beneficiario")
    private String viviendaDomicilioBarrio;

    @ExcelCellName("Vivienda_Cantidad_Ambientes")
    private Integer viviendaCantAmbientes;

    @ExcelCellName("Vivienda_Antiguedad")
    private Integer viviendaAntiguedad;

    @ExcelCellName("Egreso_Familiar_Alquiler")
    private Long viviendaMontoAlquiler;

    @ExcelCellName("Vivienda_Materiales_Chapa")
    private Integer viviendaMaterialesChapa;

    @ExcelCellName("Vivienda_Materiales_Cartón")
    private Integer viviendaMaterialesCarton;

    @ExcelCellName("Vivienda_Materiales_Madera")
    private Integer viviendaMaterialesMadera;

    @ExcelCellName("Vivienda_Materiales_Adobe")
    private Integer viviendaMaterialesAdobe;

    @ExcelCellName("Vivienda_Materiales_Ladrillo sin reboque")
    private Integer viviendaMaterialesLadrillo;

    @ExcelCellName("Vivienda_Materiales_Otro")
    private Integer viviendaMaterialesOtro;


}
