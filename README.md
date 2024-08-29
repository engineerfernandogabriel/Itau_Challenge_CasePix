[JAVA_BADGE]:https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white
[SPRING_BADGE]: https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white

<h1 align="center" style="font-weight: bold;">Itau Challenge - Case Pix 💻</h1>

<p align="center">
    <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white" alt="java">
    <img src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white" alt="spring">
</p>

<p>CASE - Desenvolvimento módulo de cadastro de chaves PIX:</p>
<p>O Pix é o arranjo de pagamentos e recebimentos instantâneos, disponível todos os dias do ano, com liquidação em tempo real de suas transações. Ou seja, permite a transferência imediata de valores entre diferentes instituições, em todos os horários e dias, entre pessoas físicas, empresas ou entes governamentais.</p>
<p>O Pix é uma forma de pagar e receber valores</p>
<p><b>Chave Pix:</b> um apelido para a conta transacional que deve ser atribuído pelo titular da conta ou representante legal/operador permissionado, usado para identificar a conta corrente do cliente recebedor por meio de uma única informação. Essa chave retornará a informação que identificará o recebedor da cobrança.</p>
<p>O formato da chave Pix indica o seu tipo, conforme regras abaixo:</p>
<ul>
    <li><b>Número de celular:</b> inicia-se com "+", seguido do código do país, DDD, e número com nove dígitos;</li>
    <li><b>E-mail:</b> contém "@", tamanho máximo de 77 caracteres;</li>
    <li><b>CPF:</b> será utilizado com 11 dígitos, com dígitos verificadores. Deve ser informado sem pontos ou traços;</li>
    <li><b>CNPJ:</b> será utilizado com 14 dígitos, com dígitos verificadores. Deve ser informado sem pontos ou traços;</li>
    <li><b>Chave Aleatória:</b> informação alfanumérica com 36 posições. Deverá ser informado sem pontuação.</li>
</ul>

<p>O cadastro é limitado a até 5 chaves por conta para Pessoa Física e até 20 chaves por conta para Pessoa Jurídica. Ao registrar uma chave Pix, as mesmas devem ser armazenadas e disponibilizadas aos correntistas para consultarem essa chave.</p>

<p><b>INCLUSÃO</b></p>
<p>Objetivo: Viabilizar a inclusão de chaves PIX, vinculando a chave a agência e conta do correntista Itaú.</p>

<p><b>ALTERAÇÃO</b></p>
<p>Objetivo: Permitir alteração do valor de uma chave registrada. Deve-se permitir alterar um email, telefone, CNPJ / CPF já cadastrado.</p>

<p><b>DELEÇÃO</b></p>
<p>Objetivo: Inativar uma chave registrada por ID. Impedindo que a mesma seja alterada ou consultada. Somente o ID da chave deve ser informado para efetivar a desativação</p>

<p><b>CONSULTA</b></p>
<p>Objetivo: Disponibilizar funcionalidades de consulta de chaves PIX por:</p>

<ol type='a'>
    <li>Consulta por ID;</li>
    <li>Consulta por Tipo de chave;</li>
    <li>Agência e Conta;</li>
    <li>Nome do correntista;</li>
    <li>Data de inclusão da chave;</li>
    <li>Data da inativação da chave.</li>
</ol>

<p>O seu objetivo é:</p>
<ol>
    <li>Escrever um código funcional de um cadastro de Chaves PIX com APIs que atendam as funcionalidades acima descritas;</li>
    <li>Utilizar preferencialmente a linguagem Java;</li>
    <li>Seu código deve possuir testes unitários (90% de cobertura);</li>
    <li>Utilizar banco de dados (livre escolha);</li>
    <li>Pode utilizar qualquer framework que esteja familiarizado;</li>
    <li>Utilizar o gerenciador de dependências de sua preferência (maven/gradle);</li>
    <li>Utilizar um pattern de desenvolvimento que faça sentido, dado o contexto;</li>
    <li>Versionar seu código em algum controlador de versão (github, CVS etc...);</li>
    <li>Descrever/demonstrar qual(is) práticas/fatores da metodologia 12 (Twelve) Factor App foram utilizados na sua solução.</li>
</ol>

<pre><code>Você irá demonstrar funcionalmente a sua aplicação para a banca; 
Ter em mãos um client de API’s (POSTMAN por exemplo) para uso e demonstração das API’s construídas. </code></pre>
---

<p align="center">
    🔗 Desenvolvido por <a href="https://www.linkedin.com/in/enginnerfernandogabriel/" target="_blank">Fernando Gabriel (Dev Samurai)</a>
</p>