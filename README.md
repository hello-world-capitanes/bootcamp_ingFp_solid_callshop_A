# Sistema de gestion de locutorios telefónicos

## El ejemplo

Se trata de un ejemplo inspirado en una aplicación real. Se trata de un controlador para locutorios telefónicos sobre VoIP

La aplicación original controla los routers, lleva un plan de numeración, gestiona los diferentes puestos de llamadas, etc. 
No obstante, el ejemplo se limita al subsistema de tarificación

## Subsistema de tarificación (Rater)
El plan de numeración (subsistema que excede a nuestro ejemplo) define los prefijos de los números telefónicos (nacionales, locales, etc)
y es capaz de, dada una llamada, determinar a que nodo del plan de numeración corresponde.

Por ejemplo, si nuestro cliente llama al número 34911234567, el plan de numeración nos dirá que es un numero del nodo 349
(España fijo) si llama al 34611234567 correspondrá al nodo 346 (España móvil) y si llama al 54111234567 corresponderá al nodo 54 (Argentina)

Para poder tarificar una llamada (es decir, determinar el precio de la llamada y cobrarsela al cliente) necesitamos tarifas.
Cada nodo del plan de numeración tiene asociado una tarifa. Por ejemplo, el nodo 349 (España fijo) puede tener una tarifa de 0.02 euros 
por minuto + 0.10 de establecimiento de llamada.

El subsistema de tarificación (Rater) se encarga de crear dichas tarifas y de tarificar las llamadas, esto es, dada una llamada y una 
tarifa, determinar el precio de la llamada aplicando esa tarifa (recordemos que es el plan de numeración el que nos dice tarifa corresponde)

## RaterEngine
En nuestro caso, hemos generado una pequeña aplicación de consola encargada de hacer llamadas a Rater para demostrar el funcionamiento del
modulo Rater. En un sistema real, tendríamos una aplicacion (o incluso varias), mucho más complejas. El modulo RaterEngine, simplemente crea
tarifas usando el submodulo Rater y luego testea la tarifas creadas.

## El problema
Necesitamos que los modelos de tarifa sean flexibles. Inicialmente tenemos un modelo de tarifas basado en precio por minuto + establecimiento
pero queremos que en el futuro podamos incorporar otros modelos de tarifa aún por determinar.

RATER debe ser capaz de proveer un mecanismo para crear dichas tarifas (en el ejemplo se hace mediante una aplicacion de consola) teniendo en cuenta
que cada modelo de tarifa puede tener sus propias reglas de tarificación y por tanto parámetros diferentes.

Incorporar nuevos modelos de tarifa no debe requerir modificar el código de la aplicación (Ni Rater ni RaterEngine)

## La solución

Para abordar esta situación se ha definido la Interfaz *Rate* que representa una tarifa. Dicha tiene un unico método *CalculatePrice*
que recibe un objeto de la clase *RateableCall* (estructura de datos con la información relevante para tarificar una llamada) y devuelve el 
precio de la llamada.

Por cada modelo de tarifa tendremos una clase que implementa la interfaz *Rate* y tendrá su propia lógica de tarificación. Una tarifa concreta es una 
instancia de alguna de estas clases

Ahora bien, cada una de estas clases puede tener sus propios parámetros. Por ello, también se ha definido la interfaz *RateFactory* que representa
una factoria de tarifas. Por cada clase *Rate* tendremos una clase *RateFactory* que se encargará de crear instancias de la clase *Rate* con los 
parametros adecuados. *RateFactory* define un metodo *makeRate* que recibe un objeto *ParameterReader* y devuelve una instancia de *Rate*.

Por último, *ParameterReader* es un interfaz que representa un lector de parametros. Cada lector de parámetros lee los parametros de una manera
concreta (de un archivo, de la linea de comandos -como es nuestro caso-, etc). *ParameterReader* tiene un metodo readParameters de recibe
una lista de parametros (nuestro parametros son objetos que tienen todo lo necesario para poder leer y validar el valor) y devuelve un objeto
*ParametersMapper* que establece una relación entre el nombre del parametro y su valor asignado. Con este mapper la factoria crea la tarifa con los
parametros adecuados.

## Se pide a los alumnos:

En dos grupos realizarán las siguientes ampliaciones

### Nueva tarifa por franjas horarias

Una nueva tarifa con tres franjas horarias. A cada franja se le asignará una tarifa previamente definida y que ya esté en el
repositorio de tarifas.

No hay opción de menos ni más franjas horarias. Exactamente tres (si solo se quieren dos, se puede asignar a dos de ellas la misma tarifa)

Cada franja tendrá al menos una hora de duración (al ir añadiendo franjas verificaremos que queda tiempo suficiente para las restantes)

El instante de inicio de una franja marca el instante de finalizacion de la anterior. Es decir, una franja termina justo en el instante anterior a comenzar la siguiente

Los datos que se deberán pedir son:

1. Hora de inicio de la primera franja
2. Nombre de la tarifa de la primera franja (deberá existir en el repositorio)
3. Hora de inicio de la segunda franja
4. Nombre de la tarifa de la segunda franja
5. Hora de comienzo de la tercera franja
6. Nombre de la tarifa de la tercera franja.

NOTA: Debemos tener en cuenta, que una franja puede comenzar en un día y terminar en el siguiente. Así si la una franja es de 22:00 a 08:00 empieza a las 10 de la noche y acaba a las 8 de la mañana


### Lectura de las tarifas, en lugar de por teclado desde un fichero JSON

El fichero JSON estará formado por objeto JSON con tres campos

{  
>   "autor": "Autor",  
>   "fecha": "Fecha_creacion_tarifa",  
>   "tarifas":[tarifa1, tarifa2....]  

}

y cada tarifa será un objeto con el siguiente formato:

{  
>   "tipo_tarifa":"identificador_tarifa(factoria)",>     
>   "parametros":  
>      {  
>        "nombre_tarifa":"nombre",
>        "nombre_parametro1":valor_parametro1,  
>        "nombre_parametro2":valor_parametro2    
>    ...  
>      }

}  



Para ellos se programará un nuevo lector de tarifas y parametros que, en lugar de ser por consola sea por lectura del fichero

