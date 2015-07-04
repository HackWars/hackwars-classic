Public Class File
    Private _Parent As Directory = Nothing
    Private _Name As String = ""
    Private _Maker As String = ""
    Private _Location As String = ""
    Private _Description As String = ""
    Private _Quantity As Integer = 0
    Private _Type As FileTypes
    Private _Price As Single = 0.0F
    Private _CPU As Single = 0.0F
    Private _Content As String = "" 'This will store the XML, this can be read however.

    Public Property Parent() As Directory
        Get
            Return _Parent
        End Get
        Set(ByVal value As Directory)
            _Parent = value
        End Set
    End Property

    Public Property Name() As String
        Get
            Return _Name
        End Get
        Set(ByVal value As String)
            _Name = value
        End Set
    End Property

    Public Property Maker() As String
        Get
            Return _Maker
        End Get
        Set(ByVal value As String)
            _Maker = value
        End Set
    End Property

    Public Property Location() As String
        Get
            Return _Location
        End Get
        Set(ByVal value As String)
            _Location = value
        End Set
    End Property

    Public Property Description() As String
        Get
            Return _Description
        End Get
        Set(ByVal value As String)
            _Description = value
        End Set
    End Property

    Public Property Quantity() As Integer
        Get
            Return _Quantity
        End Get
        Set(ByVal value As Integer)
            _Quantity = value
        End Set
    End Property

    Public Property Type() As FileTypes 'Might make this into enum
        Get
            Return _Type
        End Get
        Set(ByVal value As FileTypes)
            _Type = value
        End Set
    End Property

    Public Property Price() As Single
        Get
            Return _Price
        End Get
        Set(ByVal value As Single)
            _Price = value
        End Set
    End Property

    Public Property CPU() As Single
        Get
            Return _CPU
        End Get
        Set(ByVal value As Single)
            _CPU = value
        End Set
    End Property

    Public Property Content() As String
        Get
            Return _Content
        End Get
        Set(ByVal value As String)
            _Content = value
        End Set
    End Property

    Public Sub New(ByVal Parent As Directory)
        _Parent = Parent
    End Sub

    '<file>
    '<name><![CDATA[PCI Card.license]]></name>
    '<maker><![CDATA[High]]></maker>
    '<location><![CDATA[]]></location>
    '<description><![CDATA[Segmenting PCI Card of RAM Optimization]]></description>
    '<quantity>1</quantity>
    '<type>19</type>
    '<price>0.0</price>
    '<cpu>0.0</cpu>
    '<content>
    '<attribute0><![CDATA[1]]></attribute0>
    '<attribute1><![CDATA[2]]></attribute1>
    '<attribute2><![CDATA[]]></attribute2>
    '<quality0><![CDATA[9]]></quality0>
    '<quality1><![CDATA[6]]></quality1>
    '<quality2><![CDATA[]]></quality2>
    '<timeout><![CDATA[1207067087326]]></timeout>
    '<maxquality><![CDATA[1000.0]]></maxquality>
    '<currentquality><![CDATA[871.0]]></currentquality>
    '<lastdegrade><![CDATA[1247482040254]]></lastdegrade>
    '</content>
    '</file>
End Class
